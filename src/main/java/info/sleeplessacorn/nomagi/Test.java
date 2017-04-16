package info.sleeplessacorn.nomagi;

import com.google.common.base.Objects;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

@Mod.EventBusSubscriber
public class Test {

    @SubscribeEvent
    public static void checkOwn(PlayerInteractEvent.RightClickItem event) {
        Item item = event.getItemStack().getItem();
        Tent tent = TentWorldSavedData.getData(event.getWorld()).getTent(event.getEntityPlayer());
        if (tent == null && item == Items.STICK) {
            tent = new Tent(event.getEntityPlayer().getGameProfile().getId(), event.getEntityPlayer().chunkCoordX, event.getEntityPlayer().chunkCoordZ);
            tent.initialize(event.getEntityPlayer());
        }

        if (tent == null)
            return;

        if (item == Items.DIAMOND)
            tent.setRoom(ModObjects.STARTER, 1, 0);

        if (item == Items.APPLE)
            TentWorldSavedData.getData(event.getWorld()).setTent(event.getEntityPlayer(), null);
    }

    @SubscribeEvent
    public static void checkIn(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        Item item = event.getItemStack().getItem();
        Tent tent = TentWorldSavedData.getData(player.getEntityWorld()).getTent(player.chunkCoordX, player.chunkCoordZ);

        if (tent != null && item == Items.POTATO) {
            Nomagi.LOGGER.info("Tent owner: {}", UsernameCache.getLastKnownUsername(tent.getOwnerId()));
            Nomagi.LOGGER.info(Objects.toStringHelper(Collection.class).add("inside", tent.getPlayersInside()));
        }
    }
}
