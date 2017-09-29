package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.command.CommandBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Nomagi.ID,
     name = Nomagi.NAME,
     version = Nomagi.VERSION,
     dependencies = Nomagi.DEPENDENCIES,
     acceptedMinecraftVersions = Nomagi.MC_VERSIONS)
public class Nomagi {

    public static final String ID = "nomagi";
    public static final String NAME = "Nomagi";
    public static final String VERSION = "@VERSION@";
    public static final String MC_VERSIONS = "[1.12,1.13)";
    public static final String DEPENDENCIES = "required-after:forge@[14.21.1.2387,)";

    public static final CreativeTabs TAB = new CreativeTabs(ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModObjects.BLOCK_TENT);
        }
    };

    private static final boolean DEOBF = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @Mod.Instance(ID)
    public static Nomagi instance;

    public static boolean isDevEnv() {
        return DEOBF;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        ModGuis.register();
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        CommandBase.register(event);
    }

}
