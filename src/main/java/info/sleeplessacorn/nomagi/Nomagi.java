package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.network.MessageCreateRoom;
import info.sleeplessacorn.nomagi.proxy.GuiHandler;
import info.sleeplessacorn.nomagi.proxy.ProxyCommon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Nomagi.MODID, name = Nomagi.NAME, version = "@VERSION@")
public class Nomagi {

    public static final String MODID = "nomagi";
    public static final String NAME = "Nomagi";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final SimpleNetworkWrapper NETWORK_WRAPPER = new SimpleNetworkWrapper(MODID);
    public static final CreativeTabs TAB_NOMAGI = new CreativeTabs("nomagi") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModObjects.DOOR);
        }
    };

    @SidedProxy(clientSide = "info.sleeplessacorn.nomagi.proxy.ProxyClient", serverSide = "info.sleeplessacorn.nomagi.proxy.ProxyServer")
    public static ProxyCommon PROXY;
    @Mod.Instance(MODID)
    public static Nomagi INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PROXY.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        NETWORK_WRAPPER.registerMessage(MessageCreateRoom.Handler.class, MessageCreateRoom.class, 0, Side.SERVER);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit();
    }
}
