package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.base.BlockBase;
import info.sleeplessacorn.nomagi.client.ICustomStateMapper;
import info.sleeplessacorn.nomagi.client.WrappedModel;
import info.sleeplessacorn.nomagi.client.WrappedModel.Builder;
import info.sleeplessacorn.nomagi.client.render.MultiSelectionRenderer;
import info.sleeplessacorn.nomagi.item.base.ItemBlockBase;
import info.sleeplessacorn.nomagi.util.RayTraceHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;


public class BlockTable extends BlockBase implements MultiSelectionRenderer.IProvider, ICustomStateMapper {

    private static final ImmutableMap<EnumFacing, IProperty<Boolean>> ADJACENT_CONNECTIONS = Arrays
            .stream(EnumFacing.HORIZONTALS)
            .collect(ImmutableMap.toImmutableMap(
                    Function.identity(),
                    facing -> PropertyBool.create("connect_" + facing.getName().toLowerCase(Locale.ROOT))
            ));

    public BlockTable() {
        super("table", Material.WOOD);
        setAABB(AABB.SURFACE.get());
        setNonFullBlock();
    }

    public List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
        List<AxisAlignedBB> list = Lists.newArrayList();
        list.add(AABB.SURFACE.get());
        boolean north = state.getValue(ADJACENT_CONNECTIONS.get(EnumFacing.NORTH));
        boolean east = state.getValue(ADJACENT_CONNECTIONS.get(EnumFacing.EAST));
        boolean south = state.getValue(ADJACENT_CONNECTIONS.get(EnumFacing.SOUTH));
        boolean west = state.getValue(ADJACENT_CONNECTIONS.get(EnumFacing.WEST));
        if (!north && !west) list.add(AABB.LEG_NW.get());
        if (!north && !east) list.add(AABB.LEG_NE.get());
        if (!south && !west) list.add(AABB.LEG_SW.get());
        if (!south && !east) list.add(AABB.LEG_SE.get());
        return list;
    }

    @SideOnly(Side.CLIENT)
    public IStateMapper getCustomMapper() {
        StateMap.Builder builder = new StateMap.Builder();
        ADJACENT_CONNECTIONS.values().forEach(builder::ignore);
        return builder.build();
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this) {
            @Override
            public void getModels(Set<WrappedModel> models) {
                ResourceLocation path = new ResourceLocation(Nomagi.ID, "table_item");
                models.add(new Builder(this).setResourceLocation(path).build());
            }
        };
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState actualState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        BlockPos posAt = pos.offset(side);
        IBlockState stateAt = world.getBlockState(posAt);
        boolean shouldRender = stateAt.doesSideBlockRendering(world, posAt, side.getOpposite());
        return world.getBlockState(pos) == stateAt ? side == EnumFacing.UP : !shouldRender;
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.UP ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        IProperty[] connections = ADJACENT_CONNECTIONS.values().toArray(new IProperty[0]);
        return super.createStateContainer().add(connections);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            IBlockState stateAt = world.getBlockState(pos.offset(side));
            boolean isTable = stateAt.getBlock() instanceof BlockTable;
            state = state.withProperty(ADJACENT_CONNECTIONS.get(side), isTable);
        }
        return state;
    }

    @Override
    @Deprecated
    public void addCollisionBoxToList(
            IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes,
            Entity entity, boolean isActualState) {
        if (!isActualState) state = state.getActualState(world, pos);
        for (AxisAlignedBB aabb : getCollisionBoxList(state)) {
            AxisAlignedBB aabbAt = aabb.offset(pos);
            if (entityBox.intersects(aabbAt)) {
                collidingBoxes.add(aabbAt);
            }
        }
    }

    @Override
    @Deprecated
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
        List<AxisAlignedBB> aabbs = getCollisionBoxList(state.getActualState(world, pos));
        return RayTraceHelper.rayTraceMultiAABB(aabbs, pos, start, end);
    }

    @Override
    public List<AxisAlignedBB> apply(IBlockState state) {
        return getCollisionBoxList(state);
    }

    public enum AABB {

        SURFACE(new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D)),
        LEG_NW(new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.3125D, 0.8125D, 0.3125D)),
        LEG_NE(new AxisAlignedBB(0.6875D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.3125D)),
        LEG_SW(new AxisAlignedBB(0.125D, 0.0D, 0.6875D, 0.3125D, 0.8125D, 0.875D)),
        LEG_SE(new AxisAlignedBB(0.6875D, 0.0D, 0.6875D, 0.875D, 0.8125D, 0.875D));

        private final AxisAlignedBB aabb;

        AABB(AxisAlignedBB aabb) {
            this.aabb = aabb;
        }

        public AxisAlignedBB get() {
            return aabb;
        }

    }

}
