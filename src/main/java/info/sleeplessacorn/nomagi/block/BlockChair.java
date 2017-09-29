package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import info.sleeplessacorn.nomagi.ModRegistry;
import info.sleeplessacorn.nomagi.block.base.BlockEnumCardinalBase;
import info.sleeplessacorn.nomagi.entity.EntityChair;
import info.sleeplessacorn.nomagi.item.ItemBlockChair;
import info.sleeplessacorn.nomagi.util.BoundingBoxHelper;
import info.sleeplessacorn.nomagi.util.IStatePropertyHolder;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChair extends BlockEnumCardinalBase<BlockChair.Half> {

    private static final PropertyBool HAS_TABLE = PropertyBool.create("has_table");

    public BlockChair() {
        super("chair", "half", Half.class);
    }

    @Override
    public void registerItemBlock() {
        ModRegistry.registerItemBlock(new ItemBlockChair(this));
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        return super.createStateContainer().add(HAS_TABLE);
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (getType(state) == Half.UPPER) pos = pos.down();
        IBlockState stateAt = world.getBlockState(pos.offset(getFacing(state)));
        return state.withProperty(HAS_TABLE, stateAt.getBlock() instanceof BlockTable);
    }

    @Override
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getType(state).getBoundingBox(getFacing(state));
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(
            IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.setBlockToAir(getType(state) == Half.UPPER ? pos.down() : pos.up());
    }

    @Override
    public boolean onBlockActivated(
            World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing,
            float hitX, float hitY, float hitZ) {
        if (world.isRemote) return false;
        if (!player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
            BlockPos entityPos = getType(state) == Half.LOWER ? pos : pos.down();
            AxisAlignedBB range = new AxisAlignedBB(entityPos, entityPos.add(1, 1, 1));
            if (world.getEntitiesWithinAABB(EntityChair.class, range).isEmpty()) {
                EntityChair entityChair = new EntityChair(world, entityPos);
                world.spawnEntity(entityChair);
                player.startRiding(entityChair);
            }
            return true;
        }
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this)); // TODO: Handle this in the base class with a setter
    }

    @Override
    public void getDrops(
            NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(this)); // TODO: Handle this in the base class with a setter
    }

    @Override
    public ItemStack getPickBlock(
            IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this); // TODO: Handle this in the base class with a setter
    }

    public enum Half implements IStatePropertyHolder<Half> {

        LOWER(BoundingBoxHelper.computeAABBsForFacing(1, 0, 2, 15, 15, 14)),
        UPPER(BoundingBoxHelper.computeAABBsForFacing(2, -1, 12, 14, 9, 14));

        private final ImmutableMap<EnumFacing, AxisAlignedBB> boundingBoxes;

        Half(ImmutableMap<EnumFacing, AxisAlignedBB> boundingBoxes) {
            this.boundingBoxes = boundingBoxes;
        }

        @Override
        public Half getEnum() {
            return this;
        }

        @Override
        public Material getMaterial() {
            return Material.WOOD;
        }

        @Override
        public int getLightOpacity() {
            return 0;
        }

        @Override
        public boolean isFullCube() {
            return false;
        }

        public AxisAlignedBB getBoundingBox(EnumFacing facing) {
            return boundingBoxes.get(facing);
        }

    }

}
