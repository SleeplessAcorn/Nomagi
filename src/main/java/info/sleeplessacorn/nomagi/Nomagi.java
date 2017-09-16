package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.command.CommandNomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.network.MessageCreateRoom;
import info.sleeplessacorn.nomagi.network.MessageOpenCreateRoomGui;
import info.sleeplessacorn.nomagi.network.MessageOpenPrivacyGui;
import info.sleeplessacorn.nomagi.network.MessageUpdateUsernames;
import info.sleeplessacorn.nomagi.proxy.ProxyCommon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Nomagi.MOD_ID, name = Nomagi.MOD_NAME, version = Nomagi.MOD_VERSION)
public class Nomagi {

    @Mod.Instance(Nomagi.MOD_ID)
    public static Nomagi instance;

    public static final String MOD_ID = "nomagi";
    public static final String MOD_NAME = "Nomagi";
    public static final String MOD_VERSION = "@VERSION@";
    public static final Logger LOGGER = LogManager.getLogger(Nomagi.MOD_NAME);
    public static final SimpleNetworkWrapper NET_WRAPPER = new SimpleNetworkWrapper(Nomagi.MOD_ID);
    public static final CreativeTabs TAB_NOMAGI = new CreativeTabs(Nomagi.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModObjects.DOOR);
        }
    };

    @SidedProxy(clientSide = "info.sleeplessacorn.nomagi.proxy.ProxyClient")
    public static ProxyCommon proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        proxy.onPreInit(event);
        NET_WRAPPER.registerMessage(MessageCreateRoom.Handler.class, MessageCreateRoom.class, 0, Side.SERVER);
        NET_WRAPPER.registerMessage(MessageOpenCreateRoomGui.Handler.class, MessageOpenCreateRoomGui.class, 1, Side.CLIENT);
        NET_WRAPPER.registerMessage(MessageOpenPrivacyGui.Handler.class, MessageOpenPrivacyGui.class, 2, Side.CLIENT);
        NET_WRAPPER.registerMessage(MessageUpdateUsernames.Handler.class, MessageUpdateUsernames.class, 3, Side.CLIENT);
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.onPostInit(event);
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandNomagi());
    }

    public static class ServerProxy extends ProxyCommon {
        // dummy class
    }

}
