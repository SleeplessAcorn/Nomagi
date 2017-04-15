package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Nomagi.MODID, name = Nomagi.NAME, version = "@VERSION@")
public class Nomagi {

    public static final String MODID = "nomagi";
    public static final String NAME = "Nomagi";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModObjects.preInit();
    }

    @Mod.EventBusSubscriber
    public static class test {
        @SubscribeEvent
        public static void onPlayerInteractRightClickItem(PlayerInteractEvent.RightClickItem event) {
            Item item = event.getItemStack().getItem();
            Tent tent = TentWorldSavedData.getData(event.getWorld()).getTent(event.getEntityPlayer());
            if (tent == null && item == Items.STICK) {
                tent = new Tent(event.getEntityPlayer().chunkCoordX, event.getEntityPlayer().chunkCoordZ);
                tent.initialize(event.getEntityPlayer());
            }

            if (tent == null)
                return;

            if (item == Items.DIAMOND)
                tent.setRoom(ModObjects.STARTER, 1, 0);

            if (item == Items.APPLE)
                TentWorldSavedData.getData(event.getWorld()).setTent(event.getEntityPlayer(), null);
        }
    }
}
