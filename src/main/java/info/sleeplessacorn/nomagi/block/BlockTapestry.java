package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tehnut.lib.mc.block.BlockAxisY;
import tehnut.lib.mc.model.IModeled;

import java.util.List;

public class BlockTapestry extends BlockAxisY implements IModeled {

    public static final IProperty<Boolean> TOP = PropertyBool.create("top");
    private static final ImmutableMap<EnumFacing, AxisAlignedBB> AABB_TAPESTRY = ImmutableMap.of(
            EnumFacing.NORTH, new AxisAlignedBB(0.00D, 0.00D, 0.75D, 1.00D, 1.00D, 1.00D),
            EnumFacing.EAST, new AxisAlignedBB(0.00D, 0.00D, 0.00D, 0.25D, 1.00D, 1.00D),
            EnumFacing.SOUTH, new AxisAlignedBB(0.00D, 0.00D, 0.00D, 1.00D, 1.00D, 0.25D),
            EnumFacing.WEST, new AxisAlignedBB(0.75D, 0.00D, 0.00D, 1.00D, 1.00D, 1.00D));

    public BlockTapestry() {
        super(Material.CLOTH);
        setUnlocalizedName(Nomagi.ID + ".tapestry");
        setCreativeTab(Nomagi.CTAB);
        setSoundType(SoundType.CLOTH);
        setHardness(0.8F);
        setResistance(4.0F);
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
        boolean top = meta >> 2 == 1;
        return getDefaultState().withProperty(getProperty(), facing).withProperty(TOP, top);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(getProperty()).getHorizontalIndex();
        int top = (state.getValue(TOP) ? 1 : 0) << 2;
        return facing | top;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_TAPESTRY.getOrDefault(state.getValue(getProperty()), FULL_BLOCK_AABB);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!state.getValue(TOP)) return;
        EnumFacing facing = state.getValue(getProperty());
        BlockPos posAt = pos.offset(facing.getOpposite());
        if (!world.getBlockState(posAt).isSideSolid(world, posAt, facing)) {
            world.setBlockToAir(pos);
            world.setBlockToAir(pos.down());
            dropBlockAsItem(world, pos, state, 0);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.destroyBlock(state.getValue(TOP) ? pos.down() : pos.up(), false);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), TOP).build();
    }

    @Override
    public IBlockState getStateForPlacement(
            World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(getProperty(), facing).withProperty(TOP, meta > 0);
    }

    @Override
    public void getVariants(List<String> variants) {
        variants.add("inventory");
    }

}
