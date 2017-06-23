package info.sleeplessacorn.nomagi.network;

import com.google.common.collect.Maps;
import info.sleeplessacorn.nomagi.client.gui.GuiTentPrivacy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;
import java.util.UUID;

public class MessageUpdateUsernames implements IMessage {

    private Map<UUID, String> cache;

    public MessageUpdateUsernames() {
        cache = UsernameCache.getMap();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        cache = Maps.newHashMap();
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
            cache.put(UUID.fromString(ByteBufUtils.readUTF8String(buf)), ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(UsernameCache.getMap().size());
        for (Map.Entry<UUID, String> entry : cache.entrySet()) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            for (EntityPlayerMP player : server.getPlayerList().getPlayers()) {
                if (player.getGameProfile().getId().equals(entry.getKey())) { // Only send values for online players
                    ByteBufUtils.writeUTF8String(buf, entry.getKey().toString());
                    ByteBufUtils.writeUTF8String(buf, entry.getValue());
                    break;
                }
            }
        }
    }

    public static class Handler implements IMessageHandler<MessageUpdateUsernames, IMessage> {
        @Override
        public IMessage onMessage(MessageUpdateUsernames message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> GuiTentPrivacy.usernameCache = message.cache);
            return null;
        }
    }
}
