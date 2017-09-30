package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.client.GuiHandler;
import info.sleeplessacorn.nomagi.command.CommandNomagi;
import info.sleeplessacorn.nomagi.core.RegistrarNomagi;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Nomagi.ID, name = Nomagi.NAME, version = Nomagi.VERSION, dependencies = Nomagi.DEPENDENCIES, acceptedMinecraftVersions = Nomagi.MC_VERSIONS)
public class Nomagi {

    public static final String ID = "nomagi";
    public static final String NAME = "Nomagi";
    public static final String VERSION = "@VERSION@";
    public static final String MC_VERSIONS = "[1.12,1.13)";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static final CreativeTabs TAB = new CreativeTabs(ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(RegistrarNomagi.TENT);
        }
    };

    public static final boolean IS_DEV = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @Mod.Instance(ID)
    public static Nomagi INSTANCE;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        GuiHandler.register();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandNomagi());
    }

    public static void debug(String message, Object... format) {
        if (IS_DEV)
            LOGGER.info("[DEBUG] " + message, format);
    }
}
