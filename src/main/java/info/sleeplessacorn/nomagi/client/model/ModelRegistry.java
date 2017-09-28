package info.sleeplessacorn.nomagi.client.model;

import info.sleeplessacorn.nomagi.ModObjects;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.client.render.RenderLecternBook;
import info.sleeplessacorn.nomagi.client.render.RenderWorktableSchematic;
import info.sleeplessacorn.nomagi.common.tile.TilePrivacyLectern;
import info.sleeplessacorn.nomagi.common.tile.TileRoomWorktable;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Nomagi.ID, value = Side.CLIENT)
public class ModelRegistry {

    private static final Set<WrappedModel> MODELS = new HashSet<>();
    private static final Set<Class<? extends StateMapperBase>> STATE_MAPPERS = new HashSet<>();

    public static void registerModel(WrappedModel model) {
        MODELS.add(model);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModObjects.BLOCK_TABLE.registerStateMapper();
        ClientRegistry.bindTileEntitySpecialRenderer(TilePrivacyLectern.class, new RenderLecternBook());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRoomWorktable.class, new RenderWorktableSchematic());
        MODELS.forEach(model -> ModelLoader.setCustomModelResourceLocation(
                model.getItem(), model.getMetadata(), model.getMRL())
        );
    }

}
