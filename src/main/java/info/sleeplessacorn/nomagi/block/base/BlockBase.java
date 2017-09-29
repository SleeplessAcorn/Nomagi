package info.sleeplessacorn.nomagi.block.base;

import info.sleeplessacorn.nomagi.ModRegistry;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.item.base.ItemBlockBase;
import info.sleeplessacorn.nomagi.util.MaterialHelper;
import info.sleeplessacorn.nomagi.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block {

    protected final BlockStateContainer container;

    protected BlockRenderLayer layer = BlockRenderLayer.SOLID;
    protected AxisAlignedBB aabb = Block.FULL_BLOCK_AABB;
    protected boolean fullBlock = true;

    public BlockBase(String name, float hardness, float resistance, Material material, SoundType sound) {
        super(material);
        container = createStateContainer().build();
        setRegistryName(name);
        setUnlocalizedName(Nomagi.ID + "." + StringHelper.toLangKey(name));
        setHardness(hardness);
        setResistance(resistance);
        setSoundType(sound);
        setCreativeTab(Nomagi.TAB);
        setDefaultState(getBlockState().getBaseState());
        registerItemBlock();
    }

    public BlockBase(String name, Material material, SoundType sound) {
        this(name, MaterialHelper.getHardness(material), MaterialHelper.getResistance(material), material, sound);
    }

    public BlockBase(String name, float hardness, float resistance, Material material) {
        this(name, hardness, resistance, material, MaterialHelper.getSoundType(material));
    }

    public BlockBase(String name, Material material) {
        this(name, material, MaterialHelper.getSoundType(material));
    }

    protected void registerItemBlock() {
        ModRegistry.registerItemBlock(new ItemBlockBase(this));
    }

    protected final void setRenderLayer(BlockRenderLayer layer) {
        this.layer = layer;
    }

    protected void setAABB(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    protected final void setNonFullBlock() {
        this.fullBlock = false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return fullBlock;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return aabb;
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return state != world.getBlockState(pos.offset(side)) && super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(
            IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return fullBlock ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return fullBlock;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return layer;
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).build();
    }

    @Override
    public final BlockStateContainer getBlockState() {
        return container;
    }

    protected BlockStateContainer.Builder createStateContainer() {
        return new BlockStateContainer.Builder(this);
    }

}
