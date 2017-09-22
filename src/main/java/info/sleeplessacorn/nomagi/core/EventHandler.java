package info.sleeplessacorn.nomagi.core;

import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (true)
            return; // FIXME

        TentWorldSavedData savedData = TentWorldSavedData.getData(event.getWorld());
        Tent tent = savedData.getTent(event.getPos().getX() / 16, event.getPos().getZ() / 16);

        if (tent == null)

        if (tent != null) {
            Room room = tent.getRoom(event.getPlayer());
            if (room != null) {
                boolean canPlace = room.getCustomization().canModify(
                        event.getWorld(), event.getPos(), event.getState());
                if (!canPlace) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (true)
            return; // FIXME

        TentWorldSavedData savedData = TentWorldSavedData.getData(event.getWorld());
        Tent tent = savedData.getTent(event.getPos().getX() / 16, event.getPos().getZ() / 16);

        if (tent == null)
            return;

        Room room = tent.getRoom(event.getPlayer());
        if (room != null) {
            boolean canPlace = room.getCustomization().canModify(
                    event.getWorld(), event.getPos(), event.getState());
            if (!canPlace) {
                event.setCanceled(true);
            }
        }
    }
}
