package info.sleeplessacorn.nomagi.util;

import info.sleeplessacorn.nomagi.tile.ITileProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class TileHelper {

    private static boolean isTileHolder(Block block) {
        return block instanceof ITileProvider;
    }

    public static boolean hasTileEntity(IBlockState state) {
        return getTileEntity(state) != null;
    }

    public static TileEntity getTileEntity(IBlockState state) {
        return state.getBlock() instanceof ITileProvider
               ? ((ITileProvider) state.getBlock()).getTileEntity(state) : null;
    }

    public static boolean onTileInteract(IBlockState state, World world, BlockPos pos, EntityPlayer player) {
        return isTileHolder(state.getBlock()) && ((ITileProvider) state.getBlock())
                .onTileInteract(state, world, pos, player);
    }

    public static void onTileRemove(IBlockState state, World world, BlockPos pos) {
        if (isTileHolder(state.getBlock())) {
            ((ITileProvider) state.getBlock()).onTileRemove(state, world, pos);
            world.removeTileEntity(pos);
        }
    }

}

