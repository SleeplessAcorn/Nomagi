package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTent extends Block {

    public BlockTent() {
        super(Material.CLOTH);
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".tent");
        setSoundType(SoundType.CLOTH);
    }

    @Override public boolean isFullBlock(IBlockState state) { return false; }
    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public AxisAlignedBB getBoundingBox(
            IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(-0.5D, 0D, 0D, 1.5D, 1.75D, 2D);
    }

}
