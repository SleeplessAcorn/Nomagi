package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tehnut.lib.mc.model.IModeled;

import java.util.List;

public class BlockTapestry extends Block implements IModeled {

    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final IProperty<Boolean> TOP = PropertyBool.create("top");

    public BlockTapestry() {
        super(Material.CLOTH);

        setUnlocalizedName(Nomagi.MOD_ID + ".tapestry");
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setSoundType(SoundType.CLOTH);
        setHardness(0.2F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1D, 1D, 1D);
            case EAST:
                return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.25D, 1D, 1D);
            case SOUTH:
                return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 1D, 0.25D);
            case WEST:
                return new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1D, 1D, 1D);
        }
        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.destroyBlock(pos.offset(state.getValue(TOP) ? EnumFacing.DOWN : EnumFacing.UP), false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.HORIZONTALS[meta % 4];
        boolean top = false;
        if (meta >= 4)
            top = true;

        return getDefaultState().withProperty(FACING, facing).withProperty(TOP, top);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case NORTH:
                meta = 0;
                break;
            case EAST:
                meta = 1;
                break;
            case SOUTH:
                meta = 2;
                break;
            case WEST:
                meta = 3;
                break;
        }
        if (state.getValue(TOP))
            meta += 4;

        return meta;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!state.getValue(TOP))
            return;

        BlockPos reversePos = pos.offset(state.getValue(FACING).getOpposite());
        IBlockState reverseState = world.getBlockState(reversePos);
        if (!reverseState.isSideSolid(world, reversePos, state.getValue(FACING))) {
            world.setBlockToAir(pos.down());
            world.setBlockToAir(pos);
            dropBlockAsItem(world, pos, state, 0);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(FACING, TOP).build();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING, facing).withProperty(TOP, meta == 1);
    }

    @Override
    public void getVariants(List<String> variants) {
        variants.add("inventory");
    }
}
