package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.base.BlockEnumCardinalBase;
import info.sleeplessacorn.nomagi.block.base.IPropertyProvider;
import info.sleeplessacorn.nomagi.client.GuiHandler;
import info.sleeplessacorn.nomagi.item.ItemBlockRoomWorktable;
import info.sleeplessacorn.nomagi.tile.TileRoomWorktable;
import info.sleeplessacorn.nomagi.util.BoundingBoxHelper;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import javax.annotation.Nullable;

public class BlockRoomWorktable extends BlockEnumCardinalBase<BlockRoomWorktable.Side> {

    public BlockRoomWorktable() {
        super("room_worktable", "side", Side.class);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockRoomWorktable(this);
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getType(state).getBoundingBox(getFacing(state));
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this)); // TODO: Handle this in the base class as a block class toggle
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(this)); // TODO: Handle this in the base class as a block class toggle
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this); // TODO: Handle this in the base class as a block class toggle
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.setBlockToAir(getType(state) == Side.LEFT ? pos.offset(getFacing(state).rotateYCCW()) : pos.offset(getFacing(state).rotateY()));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (getType(state) == Side.RIGHT)
            pos = pos.offset(getFacing(state).rotateY());

        if (world.getTileEntity(pos) != null) {
            int id = GuiHandler.ROOM_WORKTABLE.ordinal();
            FMLNetworkHandler.openGui(player, Nomagi.INSTANCE, id, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }

    @Override
    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getType(state) == Side.LEFT;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getType(state) == Side.LEFT ? new TileRoomWorktable() : null;
    }

    public enum Side implements IPropertyProvider<Side> {

        LEFT(ImmutableMap.of(
                EnumFacing.NORTH, BoundingBoxHelper.createAABB(-16, 0, 0, 16, 16, 16),
                EnumFacing.SOUTH, BoundingBoxHelper.createAABB(0, 0, 0, 32, 16, 16),
                EnumFacing.EAST, BoundingBoxHelper.createAABB(0, 0, -16, 16, 16, 16),
                EnumFacing.WEST, BoundingBoxHelper.createAABB(0, 0, 0, 16, 16, 32)
        )),
        RIGHT(ImmutableMap.of(
                EnumFacing.NORTH, BoundingBoxHelper.createAABB(0, 0, 0, 32, 16, 16),
                EnumFacing.SOUTH, BoundingBoxHelper.createAABB(-16, 0, 0, 16, 16, 16),
                EnumFacing.EAST, BoundingBoxHelper.createAABB(0, 0, 0, 16, 16, 32),
                EnumFacing.WEST, BoundingBoxHelper.createAABB(0, 0, -16, 16, 16, 16)
        ));

        private final ImmutableMap<EnumFacing, AxisAlignedBB> boundingBoxes;

        Side(ImmutableMap<EnumFacing, AxisAlignedBB> boundingBoxes) {
            this.boundingBoxes = boundingBoxes;
        }

        @Override
        public Side getProvider() {
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

        @Override
        public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
            return false;
        }

        public AxisAlignedBB getBoundingBox(EnumFacing facing) {
            return boundingBoxes.get(facing);
        }
    }
}
