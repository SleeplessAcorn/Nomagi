package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.base.BlockCardinalBase;
import info.sleeplessacorn.nomagi.client.WrappedModel;
import info.sleeplessacorn.nomagi.client.WrappedModel.Builder;
import info.sleeplessacorn.nomagi.client.render.MultiSelectionRenderer;
import info.sleeplessacorn.nomagi.item.base.ItemBlockBase;
import info.sleeplessacorn.nomagi.util.BoundingBoxHelper;
import info.sleeplessacorn.nomagi.util.RayTraceHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class BlockShelf extends BlockCardinalBase implements MultiSelectionRenderer.IProvider {

    private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    private static final PropertyBool CONNECT_L = PropertyBool.create("connect_left");
    private static final PropertyBool CONNECT_R = PropertyBool.create("connect_right");

    public BlockShelf() {
        super("shelf", Material.WOOD);
        setNonFullBlock();
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this) {
            @Override
            public void gatherModels(Set<WrappedModel> models) {
                ResourceLocation path = new ResourceLocation(Nomagi.ID, "shelf_item");
                models.add(new Builder(this).setResourceLocation(path).build());
            }
        };
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        return super.createStateContainer().add(CONNECT_R).add(CONNECT_L);
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState stateLeft = world.getBlockState(pos.offset(getFacing(state).rotateYCCW()));
        IBlockState stateRight = world.getBlockState(pos.offset(getFacing(state).rotateY()));
        state = state.withProperty(CONNECT_L, doShelvesMatch(state, stateLeft));
        state = state.withProperty(CONNECT_R, doShelvesMatch(state, stateRight));
        return state;
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState) {
        if (!isActualState)
            state = state.getActualState(world, pos);

        for (AxisAlignedBB aabb : getCollisionBoxList(state)) {
            AxisAlignedBB aabbAt = aabb.offset(pos);
            if (entityBox.intersects(aabbAt))
                collidingBoxes.add(aabbAt);
        }
    }

    @Override
    @Deprecated
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return EMPTY_AABB;
    }

    @Override
    @Deprecated
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        List<AxisAlignedBB> aabbs = getCollisionBoxList(state.getActualState(world, pos));
        return RayTraceHelper.rayTraceMultiAABB(aabbs, pos, start, end);
    }

    private boolean doShelvesMatch(IBlockState state, IBlockState stateAt) {
        return stateAt.getBlock() instanceof BlockShelf && getFacing(state).equals(getFacing(stateAt));
    }

    public List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
        List<AxisAlignedBB> list = Lists.newArrayList();
        EnumFacing facing = getFacing(state);
        boolean connectNone = state.getValue(CONNECT_R) && state.getValue(CONNECT_L);
        boolean connectRight = state.getValue(CONNECT_R) && !state.getValue(CONNECT_L);
        boolean connectLeft = !state.getValue(CONNECT_R) && state.getValue(CONNECT_L);
        boolean connectBoth = !state.getValue(CONNECT_R) && !state.getValue(CONNECT_L);
        if (connectNone) {
            list.add(AABB.SHELF_UPPER_NONE.get(facing));
            list.add(AABB.SHELF_LOWER_NONE.get(facing));
        } else if (connectRight) {
            if (facing.getAxis() == EnumFacing.Axis.Z) {
                list.add(AABB.RIGHT_PANEL.get(facing));
                list.add(AABB.SHELF_UPPER_RIGHT.get(facing));
                list.add(AABB.SHELF_LOWER_RIGHT.get(facing));
            } else {
                list.add(AABB.LEFT_PANEL.get(facing));
                list.add(AABB.SHELF_UPPER_LEFT.get(facing));
                list.add(AABB.SHELF_LOWER_LEFT.get(facing));
            }
        } else if (connectLeft) {
            if (facing.getAxis() == EnumFacing.Axis.Z) {
                list.add(AABB.LEFT_PANEL.get(facing));
                list.add(AABB.SHELF_UPPER_LEFT.get(facing));
                list.add(AABB.SHELF_LOWER_LEFT.get(facing));
            } else {
                list.add(AABB.RIGHT_PANEL.get(facing));
                list.add(AABB.SHELF_UPPER_RIGHT.get(facing));
                list.add(AABB.SHELF_LOWER_RIGHT.get(facing));
            }
        } else if (connectBoth) {
            list.add(AABB.LEFT_PANEL.get(facing));
            list.add(AABB.RIGHT_PANEL.get(facing));
            list.add(AABB.SHELF_UPPER_ALL.get(facing));
            list.add(AABB.SHELF_LOWER_ALL.get(facing));
        }
        return list;
    }

    @Override
    public List<AxisAlignedBB> apply(IBlockState state) {
        return getCollisionBoxList(state);
    }

    public enum AABB {

        LEFT_PANEL(BoundingBoxHelper.computeAABBsForFacing(14, 0, 6, 16, 16, 16)),
        RIGHT_PANEL(BoundingBoxHelper.computeAABBsForFacing(0, 0, 6, 2, 16, 16)),
        SHELF_LOWER_NONE(BoundingBoxHelper.computeAABBsForFacing(0, 3, 7, 16, 5, 15)),
        SHELF_UPPER_NONE(BoundingBoxHelper.computeAABBsForFacing(0, 11, 7, 16, 13, 15)),
        SHELF_LOWER_LEFT(BoundingBoxHelper.computeAABBsForFacing(0, 3, 7, 14, 5, 15)),
        SHELF_UPPER_LEFT(BoundingBoxHelper.computeAABBsForFacing(0, 11, 7, 14, 13, 15)),
        SHELF_LOWER_RIGHT(BoundingBoxHelper.computeAABBsForFacing(2, 3, 7, 16, 5, 15)),
        SHELF_UPPER_RIGHT(BoundingBoxHelper.computeAABBsForFacing(2, 11, 7, 16, 13, 15)),
        SHELF_LOWER_ALL(BoundingBoxHelper.computeAABBsForFacing(2, 3, 7, 14, 5, 15)),
        SHELF_UPPER_ALL(BoundingBoxHelper.computeAABBsForFacing(2, 11, 7, 14, 13, 15));

        private final ImmutableMap<EnumFacing, AxisAlignedBB> aabb;

        AABB(ImmutableMap<EnumFacing, AxisAlignedBB> aabb) {
            this.aabb = aabb;
        }

        public AxisAlignedBB get(EnumFacing facing) {
            return aabb.get(facing);
        }
    }
}
