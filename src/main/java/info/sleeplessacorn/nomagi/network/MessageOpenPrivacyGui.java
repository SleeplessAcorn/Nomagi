package info.sleeplessacorn.nomagi.network;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.client.gui.GuiTentPrivacy;
import info.sleeplessacorn.nomagi.core.data.Privacy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

// Fuck IGuiHandler
public class MessageOpenPrivacyGui implements IMessage {

    private Privacy privacy;
    private List<UUID> online = Lists.newArrayList();

    public MessageOpenPrivacyGui(Privacy privacy, List<UUID> online) {
        this.privacy = privacy;
        this.online = online;
    }

    public MessageOpenPrivacyGui() {}

    @Override
    @SideOnly(Side.CLIENT)
    public void fromBytes(ByteBuf buf) {
        privacy = new Privacy(Minecraft.getMinecraft().player.getGameProfile().getId());
        privacy.deserializeNBT(ByteBufUtils.readTag(buf));
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            online.add(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, privacy.serializeNBT());
        buf.writeInt(online.size());
        for (UUID uuid : online) {
            ByteBufUtils.writeUTF8String(buf, uuid.toString());
        }
    }

    public static class Handler implements IMessageHandler<MessageOpenPrivacyGui, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageOpenPrivacyGui message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(
                    new GuiTentPrivacy(message.privacy, message.online)));
            return null;
        }

    }

}
