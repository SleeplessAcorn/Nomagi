package info.sleeplessacorn.nomagi.block.base;

import info.sleeplessacorn.nomagi.client.WrappedModel;
import info.sleeplessacorn.nomagi.client.WrappedModel.Builder;
import info.sleeplessacorn.nomagi.item.base.ItemBlockEnumBase;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class BlockEnumCardinalBase<E extends Enum<E> & IPropertyProvider<E>> extends BlockEnumBase<E> {

    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockEnumCardinalBase(String name, String prefix, Class<E> clazz) {
        super(name, prefix, clazz);
    }

    public BlockEnumCardinalBase(String name, Class<E> clazz) {
        super(name, clazz);
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
    @SuppressWarnings("deprecation")
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing facing = placer.getHorizontalFacing().getOpposite();
        return super.getStateFromMeta(meta).withProperty(FACING, facing);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockEnumBase<E>(this) {
            @Override
            public void gatherModels(Set<WrappedModel> models) {
                for (E value : values)
                    models.add(new Builder(this, value.getMetadata()).addVariant("facing=north").addVariant("type=" + value.getName()).build());
            }
        };
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta >> 2);
        return super.getStateFromMeta(meta).withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = getFacing(state).getHorizontalIndex() << 2;
        return facing | super.getMetaFromState(state);
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        return super.createStateContainer().add(FACING);
    }

    public EnumFacing getFacing(IBlockState state) {
        return state.getValue(FACING);
    }

    public static PropertyDirection getFacingProperty() {
        return FACING;
    }
}

