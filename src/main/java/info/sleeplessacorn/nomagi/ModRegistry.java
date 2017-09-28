package info.sleeplessacorn.nomagi;

import info.sleeplessacorn.nomagi.common.entity.EntityChair;
import info.sleeplessacorn.nomagi.common.tile.TilePrivacyLectern;
import info.sleeplessacorn.nomagi.common.tile.TileRoomWorktable;
import info.sleeplessacorn.nomagi.common.tile.TileTent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.LinkedHashSet;

@Mod.EventBusSubscriber(modid = Nomagi.ID)
public class ModRegistry {

    private static final LinkedHashSet<ItemBlock> ITEM_BLOCKS = new LinkedHashSet<>();

    public static void registerItemBlock(Item itemBlock) {
        // Item is passed as opposed to ItemBlock to allow for vanilla's
        // base Item methods to be appended to the passed argument
        ITEM_BLOCKS.add((ItemBlock) itemBlock);
    }

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        DimensionManager.registerDimension(ModObjects.TENT_DIMENSION.getId(), ModObjects.TENT_DIMENSION);
        GameRegistry.registerTileEntity(TileTent.class, Nomagi.ID + ":tile_tent");
        GameRegistry.registerTileEntity(TileRoomWorktable.class, Nomagi.ID + ":tile_room_worktable");
        GameRegistry.registerTileEntity(TilePrivacyLectern.class, Nomagi.ID + ":tile_privacy_lectern");
        EntityRegistry.registerModEntity(EntityChair.getRegistryName(), EntityChair.class,
                EntityChair.getRegistryName().getResourcePath(), 0, Nomagi.instance, 128, 1, false);
        event.getRegistry().registerAll(
                ModObjects.BLOCK_TENT,
                ModObjects.BLOCK_DECORATIVE,
                ModObjects.BLOCK_CHAIR,
                ModObjects.BLOCK_TABLE,
                ModObjects.BLOCK_SHELF,
                ModObjects.BLOCK_CANDLE,
                ModObjects.BLOCK_ROOM_WORKTABLE,
                ModObjects.BLOCK_PRIVACY
        );
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        ITEM_BLOCKS.forEach(event.getRegistry()::register);
    }

}
