package info.sleeplessacorn.nomagi.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import info.sleeplessacorn.nomagi.ConfigHandler;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.*;
import info.sleeplessacorn.nomagi.block.base.BlockBase;
import info.sleeplessacorn.nomagi.block.base.BlockEnumBase;
import info.sleeplessacorn.nomagi.client.ICustomStateMapper;
import info.sleeplessacorn.nomagi.client.IModelProvider;
import info.sleeplessacorn.nomagi.client.WrappedModel;
import info.sleeplessacorn.nomagi.client.render.RenderLecternBook;
import info.sleeplessacorn.nomagi.client.render.RenderWorktableSchematic;
import info.sleeplessacorn.nomagi.entity.EntityChair;
import info.sleeplessacorn.nomagi.tile.TilePrivacyLectern;
import info.sleeplessacorn.nomagi.tile.TileRoomWorktable;
import info.sleeplessacorn.nomagi.tile.TileTent;
import info.sleeplessacorn.nomagi.world.WorldProviderTent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.DimensionType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

@GameRegistry.ObjectHolder(Nomagi.ID)
@Mod.EventBusSubscriber(modid = Nomagi.ID)
public class RegistrarNomagi {

    public static final Block TENT = Blocks.AIR;
    public static final Block DECORATIVE = Blocks.AIR;
    public static final Block CHAIR = Blocks.AIR;
    public static final Block TABLE = Blocks.AIR;
    public static final Block SHELF = Blocks.AIR;
    public static final Block CANDLE = Blocks.AIR;
    public static final Block ROOM_WORKTABLE = Blocks.AIR;
    public static final Block PRIVACY_LECTERN = Blocks.AIR;

    public static DimensionType tentDimension = DimensionType.register(Nomagi.ID, "_tent", ConfigHandler.tentDimensionId, WorldProviderTent.class, false);

    private static List<Item> items;
    private static List<Block> blocks;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        // :thonk:
        DimensionManager.registerDimension(RegistrarNomagi.tentDimension.getId(), RegistrarNomagi.tentDimension);

        GameRegistry.registerTileEntity(TileTent.class, Nomagi.ID + ":tent");
        GameRegistry.registerTileEntity(TileRoomWorktable.class, Nomagi.ID + ":room_worktable");
        GameRegistry.registerTileEntity(TilePrivacyLectern.class, Nomagi.ID + ":privacy_lectern");

        blocks = Lists.newArrayList(
                new BlockTent().setRegistryName("tent"),
                new BlockEnumBase<>("decorative", DecorationVariants.class).setRegistryName("decorative"),
                new BlockChair().setRegistryName("chair"),
                new BlockTable().setRegistryName("table"),
                new BlockShelf().setRegistryName("shelf"),
                new BlockCandle().setRegistryName("candle"),
                new BlockRoomWorktable().setRegistryName("room_worktable"),
                new BlockPrivacyLectern().setRegistryName("privacy_lectern")
        );

        event.getRegistry().registerAll(blocks.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        items = Lists.newArrayList(
                // For potential future items
        );

        blocks.forEach(b -> {
            if (b instanceof BlockEnumBase)
                items.add(((BlockEnumBase) b).getItemBlock().setRegistryName(b.getRegistryName()));
            else if (b instanceof BlockBase)
                items.add(((BlockBase) b).getItemBlock().setRegistryName(b.getRegistryName()));
            else
                items.add(new ItemBlock(b).setRegistryName(b.getRegistryName()));
        });

        event.getRegistry().registerAll(items.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().register(
                EntityEntryBuilder.create()
                        .entity(EntityChair.class)
                        .id(EntityChair.getRegistryName(), 0)
                        .name(EntityChair.getRegistryName().getResourcePath())
                        .tracker(128, 1, false)
                        .build()
        );
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        items.forEach(i -> {
            if (i instanceof IModelProvider) {
                Set<WrappedModel> models = Sets.newHashSet();
                ((IModelProvider) i).gatherModels(models);
                for (WrappedModel model : models)
                    ModelLoader.setCustomModelResourceLocation(i, model.getMetadata(), model.getMRL());
            } else {
                ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
            }
        });

        blocks.stream().filter(b -> b instanceof ICustomStateMapper).forEach(b -> ModelLoader.setCustomStateMapper(b, ((ICustomStateMapper) b).getCustomMapper()));

        ClientRegistry.bindTileEntitySpecialRenderer(TilePrivacyLectern.class, new RenderLecternBook());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRoomWorktable.class, new RenderWorktableSchematic());
    }
}
