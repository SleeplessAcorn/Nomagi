package info.sleeplessacorn.nomagi.network;

import info.sleeplessacorn.nomagi.client.gui.GuiRoomCreation;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// Fuck IGuiHandler
public class MessageOpenCreateRoomGui implements IMessage {

    private int chunkX;
    private int chunkZ;
    private EnumFacing facing;

    public MessageOpenCreateRoomGui(int chunkX, int chunkZ, EnumFacing facing) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.facing = facing;
    }

    public MessageOpenCreateRoomGui() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        chunkX = buf.readInt();
        chunkZ = buf.readInt();
        facing = EnumFacing.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(chunkX);
        buf.writeInt(chunkZ);
        buf.writeInt(facing.ordinal());
    }

    public static class Handler implements IMessageHandler<MessageOpenCreateRoomGui, IMessage> {

        @Override
        public IMessage onMessage(MessageOpenCreateRoomGui message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiRoomCreation(message.chunkX, message.chunkZ, message.facing)));
            return null;
        }
    }
}
