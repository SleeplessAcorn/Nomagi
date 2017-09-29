package info.sleeplessacorn.nomagi.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ITileHolder {

    @Nullable
    default TileEntity getTileEntity(IBlockState state) {
        return null;
    }

    /**
     * Called in the base block class when the tile's block is right clicked by a player
     * All parameters are provided by Block#onBlockActivated
     */
    default boolean onTileInteract(IBlockState state, World world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    /**
     * Called in the base block class when the tile's block is broken
     * All parameters are provided by Block#breakBlock
     */
    default void onTileRemove(IBlockState state, World world, BlockPos pos) {
        // no-op
    }

}

