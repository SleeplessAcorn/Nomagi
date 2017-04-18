package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Nomagi.MODID, name = Nomagi.NAME, version = "@VERSION@")
public class Nomagi {

    public static final String MODID = "nomagi";
    public static final String NAME = "Nomagi";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final CreativeTabs TAB_NOMAGI = new CreativeTabs("nomagi") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModObjects.DECOR);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModObjects.preInit();
    }
}
