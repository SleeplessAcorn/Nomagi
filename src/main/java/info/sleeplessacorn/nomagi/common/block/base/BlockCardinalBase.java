package info.sleeplessacorn.nomagi.common.block.base;

import info.sleeplessacorn.nomagi.ModRegistry;
import info.sleeplessacorn.nomagi.client.model.ModelRegistry;
import info.sleeplessacorn.nomagi.client.model.WrappedModel;
import info.sleeplessacorn.nomagi.common.item.base.ItemBlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCardinalBase extends BlockBase {

    private static final PropertyDirection FACING = PropertyDirection.create("facing", Plane.HORIZONTAL);

    public BlockCardinalBase(String name, float hardness, float resistance, Material material, SoundType sound) {
        super(name, hardness, resistance, material, sound);
    }

    public BlockCardinalBase(String name, Material material, SoundType sound) {
        super(name, material, sound);
    }

    public BlockCardinalBase(String name, float hardness, float resistance, Material material) {
        super(name, hardness, resistance, material);
    }

    public BlockCardinalBase(String name, Material material) {
        super(name, material);
    }

    public static PropertyDirection getFacingProperty() {
        return FACING;
    }

    @Override
    protected void registerItemBlock() {
        ModRegistry.registerItemBlock(new ItemBlockBase(this) {
            @Override
            protected void registerModels() {
                ModelRegistry.registerModel(new WrappedModel.Builder(this).addVariant("facing=north").build());
            }
        });
    }

    @Override
    protected BlockStateContainer.Builder createStateContainer() {
        return super.createStateContainer().add(FACING);
    }

    public EnumFacing getFacing(IBlockState state) {
        return state.getValue(FACING);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return getFacing(state).getHorizontalIndex();
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
    public IBlockState getStateForPlacement(
            World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        EnumFacing facing = placer.getHorizontalFacing().getOpposite();
        return getDefaultState().withProperty(FACING, facing);
    }

}

