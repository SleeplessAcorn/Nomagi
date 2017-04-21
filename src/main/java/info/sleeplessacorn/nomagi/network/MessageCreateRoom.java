package info.sleeplessacorn.nomagi.network;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.util.GeneratorUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCreateRoom implements IMessage {

    private Room room;
    private int chunkX;
    private int chunkZ;
    private EnumFacing direction;

    public MessageCreateRoom() {

    }

    public MessageCreateRoom(Room room, int chunkX, int chunkZ, EnumFacing direction) {
        this.room = room;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.direction = direction;
    }

    public MessageCreateRoom(int chunkX, int chunkZ, EnumFacing direction) {
        this.room = null;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.direction = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        room = ModObjects.ROOMS.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        chunkX = buf.readInt();
        chunkZ = buf.readInt();
        direction = EnumFacing.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, room == null ? "null" : room.getSchematic().toString());
        buf.writeInt(chunkX);
        buf.writeInt(chunkZ);
        buf.writeInt(direction.ordinal());
    }

    public static class Handler implements IMessageHandler<MessageCreateRoom, IMessage> {

        @Override
        public IMessage onMessage(MessageCreateRoom message, MessageContext ctx) {
            int chunkX = message.chunkX;
            int chunkZ = message.chunkZ;

            TentWorldSavedData savedData = TentWorldSavedData.getData(ctx.getServerHandler().playerEntity.getEntityWorld());
            Tent tent = savedData.getTent(ctx.getServerHandler().playerEntity);
            if (tent == null) {
                Nomagi.LOGGER.error("Tried to set a room for a Tent that didn't exist. (Owner: {})", ctx.getServerHandler().playerEntity.getGameProfile().getId());
                return null;
            }

            Room room = message.room == null ? tent.getRoom(ctx.getServerHandler().playerEntity) : message.room;

            tent.setRoom(room, chunkX, chunkZ);
            GeneratorUtil.generateRoom(ctx.getServerHandler().playerEntity.getEntityWorld(), tent, chunkX, chunkZ, room, message.direction.getOpposite());
            return null;
        }
    }
}
