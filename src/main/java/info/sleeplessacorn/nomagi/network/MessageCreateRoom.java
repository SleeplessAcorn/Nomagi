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
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageCreateRoom implements IMessage {

    private Room room;
    private int currentChunkX;
    private int currentChunkZ;
    private EnumFacing direction;

    public MessageCreateRoom() {}

    public MessageCreateRoom(Room room, int currentChunkX, int currentChunkZ, EnumFacing direction) {
        this.room = room;
        this.currentChunkX = currentChunkX;
        this.currentChunkZ = currentChunkZ;
        this.direction = direction;
    }

    public MessageCreateRoom(int currentChunkX, int currentChunkZ, EnumFacing direction) {
        this.room = null;
        this.currentChunkX = currentChunkX;
        this.currentChunkZ = currentChunkZ;
        this.direction = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        room = ModObjects.ROOMS.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        currentChunkX = buf.readInt();
        currentChunkZ = buf.readInt();
        direction = EnumFacing.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, room == null ? "null" : room.getSchematic().toString());
        buf.writeInt(currentChunkX);
        buf.writeInt(currentChunkZ);
        buf.writeInt(direction.ordinal());
    }

    public static class Handler implements IMessageHandler<MessageCreateRoom, IMessage> {

        @Override
        @SideOnly(Side.SERVER)
        public IMessage onMessage(MessageCreateRoom message, MessageContext ctx) {
            TentWorldSavedData savedData = TentWorldSavedData.getData(ctx.getServerHandler().player.getEntityWorld());
            Tent tent = savedData.getTent(ctx.getServerHandler().player);

            if (tent == null) {
                Nomagi.LOGGER.error("Tried to set a room for a Tent that didn't exist. (Owner: {})",
                        ctx.getServerHandler().player.getGameProfile().getId());
                return null;
            }

            if (!ctx.getServerHandler().player.getEntityWorld().isBlockLoaded(
                    new ChunkPos(message.currentChunkX, message.currentChunkZ).getBlock(0, 0, 0)))
                return null; // Client has requested an unloaded chunk

            Room room = message.room == null ? tent.getRoom(ctx.getServerHandler().player) : message.room;

            GeneratorUtil.generateRoom(ctx.getServerHandler().player.getEntityWorld(), tent,
                    message.currentChunkX, message.currentChunkZ, room, message.direction.getOpposite());
            savedData.markDirty();
            return null;
        }

    }

}
