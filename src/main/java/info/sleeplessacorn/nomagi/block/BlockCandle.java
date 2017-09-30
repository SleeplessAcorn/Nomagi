package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.block.base.BlockBase;
import info.sleeplessacorn.nomagi.client.WrappedModel;
import info.sleeplessacorn.nomagi.client.render.MultiSelectionRenderer;
import info.sleeplessacorn.nomagi.item.base.ItemBlockBase;
import info.sleeplessacorn.nomagi.util.RayTraceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class BlockCandle extends BlockBase implements MultiSelectionRenderer.IProvider {

    private static final PropertyBool LIT = PropertyBool.create("lit");
    private static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);

    private static final ImmutableMap<EnumFacing, AxisAlignedBB> AABB_CANDLE = ImmutableMap.of(
            EnumFacing.UP, new AxisAlignedBB(0.4375D, 0.0625D, 0.4375D, 0.5625D, 0.5625D, 0.5625D),
            EnumFacing.NORTH, new AxisAlignedBB(0.4375D, 0.375D, 0.8125D, 0.5625D, 0.875D, 0.9375D),
            EnumFacing.SOUTH, new AxisAlignedBB(0.4375D, 0.375D, 0.0625D, 0.5625D, 0.875D, 0.1875D),
            EnumFacing.WEST, new AxisAlignedBB(0.8125D, 0.375D, 0.4375D, 0.9375D, 0.875D, 0.5625D),
            EnumFacing.EAST, new AxisAlignedBB(0.0625D, 0.375D, 0.4375D, 0.1875D, 0.875D, 0.5625D)
    );

    private static final ImmutableMap<EnumFacing, AxisAlignedBB> AABB_PLATE = ImmutableMap.of(
            EnumFacing.UP, new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.125D, 0.6875D),
            EnumFacing.NORTH, new AxisAlignedBB(0.3125D, 0.25D, 0.6875D, 0.6875D, 0.4375D, 1.0D),
            EnumFacing.SOUTH, new AxisAlignedBB(0.3125D, 0.25D, 0.0D, 0.6875D, 0.4375D, 0.3125D),
            EnumFacing.WEST, new AxisAlignedBB(0.6875D, 0.25D, 0.3125D, 1.0D, 0.4375D, 0.6875D),
            EnumFacing.EAST, new AxisAlignedBB(0.0D, 0.25D, 0.3125D, 0.3125D, 0.4375D, 0.6875D)
    );

    public BlockCandle() {
        super("candle", 0.1F, 0.5F, Material.CIRCUITS, SoundType.METAL);
        setDefaultState(getDefaultState().withProperty(LIT, false));
        setRenderLayer(BlockRenderLayer.CUTOUT_MIPPED);
        setTickRandomly(true);
        setNonFullBlock();
    }

    protected boolean canPlaceOn(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().canPlaceTorchOnTop(state, world, pos);
    }

    protected boolean canPlaceAt(World world, BlockPos pos, EnumFacing facing) {
        BlockPos posAt = pos.offset(facing.getOpposite());
        IBlockState stateAt = world.getBlockState(posAt);
        BlockFaceShape faceShape = stateAt.getBlockFaceShape(world, posAt, facing);
        return canPlaceOn(world, posAt) && facing != EnumFacing.DOWN && faceShape == BlockFaceShape.SOLID;
    }

    protected void onNeighborChangeInternal(World world, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        EnumFacing.Axis axis = facing.getAxis();
        BlockPos posAt = pos.offset(facing.getOpposite());
        IBlockState stateAt = world.getBlockState(posAt);
        boolean isSolid = stateAt.getBlockFaceShape(world, posAt, facing) == BlockFaceShape.SOLID;
        if (axis.isHorizontal() && !isSolid || axis.isVertical() && !canPlaceOn(world, posAt)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    public List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
        List<AxisAlignedBB> list = Lists.newArrayList();
        EnumFacing facing = state.getValue(FACING);
        list.add(AABB_CANDLE.get(facing));
        list.add(AABB_PLATE.get(facing));
        return list;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this) {
            @Override
            public void gatherModels(Set<WrappedModel> models) {
                models.add(new WrappedModel.Builder(this).addVariant("facing=up,lit=false").build());
            }
        };
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB_CANDLE.get(state.getValue(FACING));
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        world.setBlockState(pos, state.withProperty(LIT, !state.getValue(LIT)));
        SoundEvent sound = state.getValue(LIT) ? SoundEvents.BLOCK_CLOTH_HIT : SoundEvents.ITEM_FLINTANDSTEEL_USE;
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.5F, 0.8F);
        return true;
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        return super.createStateContainer().add(FACING).add(LIT);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.values()[meta & 0b111];
        boolean lit = meta >> 0b11 == 1;
        return getDefaultState().withProperty(FACING, facing).withProperty(LIT, lit);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(FACING).ordinal();
        int lit = (state.getValue(LIT) ? 1 : 0) << 0b11;
        return facing | lit;
    }

    @Override
    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @Deprecated
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    @Deprecated
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB_PLATE.get(state.getValue(FACING));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (!state.getValue(LIT)) return;
        double posX = pos.getX() + 0.50D;
        double posY = pos.getY() + 0.625;
        double posZ = pos.getZ() + 0.50D;
        EnumFacing facing = state.getValue(FACING);
        if (facing.getAxis().isHorizontal()) {
            posX += 0.375 * facing.getOpposite().getFrontOffsetX();
            posY += 0.3125;
            posZ += 0.375 * facing.getOpposite().getFrontOffsetZ();
        }
        if (world.isRemote) {
            world.spawnParticle(EnumParticleTypes.FLAME, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        onNeighborChangeInternal(world, pos, state);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!canPlaceAt(world, pos, state.getValue(FACING))) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    @Deprecated
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        return RayTraceHelper.rayTraceMultiAABB(getCollisionBoxList(state), pos, start, end);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        for (EnumFacing side : FACING.getAllowedValues())
            if (canPlaceAt(world, pos, side))
                return true;

        return false;
    }

    @Override
    @Deprecated
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (canPlaceAt(world, pos, facing))
            return getDefaultState().withProperty(FACING, facing);

        for (EnumFacing face : EnumFacing.Plane.HORIZONTAL)
            if (canPlaceAt(world, pos, face))
                return getDefaultState().withProperty(FACING, face);

        return getDefaultState().withProperty(FACING, EnumFacing.UP);
    }

    @Override
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIT) ? 10 : 0;
    }

    @Override
    public List<AxisAlignedBB> apply(IBlockState state) {
        return getCollisionBoxList(state);
    }
}
