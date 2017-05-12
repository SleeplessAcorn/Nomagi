package info.sleeplessacorn.nomagi.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

/**
 * Used for blocks that implement the Chisel CTM format
 *
 * When Chisel is not loaded, a secondary blockstate file, with {@code _noctm} appended, is used instead.
 */
public class StateMapperNoCTM extends StateMapperBase {

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        ResourceLocation location = state.getBlock().getRegistryName();
        if (!Loader.isModLoaded("chisel"))
            location = new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "_noctm");

        return new ModelResourceLocation(location, getPropertyString(state.getProperties()));
    }
}
