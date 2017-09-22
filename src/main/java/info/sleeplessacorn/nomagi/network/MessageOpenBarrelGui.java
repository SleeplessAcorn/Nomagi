package info.sleeplessacorn.nomagi.network;

import info.sleeplessacorn.nomagi.block.barrel.GuiBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Fuck IGuiHandler
public class MessageOpenBarrelGui implements IMessage {

    private TileEntity tile;
    private EntityPlayer player;

    public MessageOpenBarrelGui(TileEntity tile, EntityPlayer player) {
        this.tile = tile;
        this.player = player;
    }

    public MessageOpenBarrelGui() {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<MessageOpenBarrelGui, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageOpenBarrelGui message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiBarrel(message.tile, message.player)));
            return null;
        }
    }
}
