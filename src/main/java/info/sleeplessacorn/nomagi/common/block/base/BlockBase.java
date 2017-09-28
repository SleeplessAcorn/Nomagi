package info.sleeplessacorn.nomagi.common.block.base;

import info.sleeplessacorn.nomagi.ModRegistry;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.common.item.base.ItemBlockBase;
import info.sleeplessacorn.nomagi.common.util.MaterialHelper;
import info.sleeplessacorn.nomagi.common.util.StringHelper;
import info.sleeplessacorn.nomagi.common.util.TileHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

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
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileHelper.onTileRemove(state, world, pos);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return layer;
    }

    @Override
    public boolean onBlockActivated(
            World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing,
            float hitX, float hitY, float hitZ) {
        return TileHelper.onTileInteract(state, world, pos, player);
    }

    @Override
    protected final BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).build();
    }

    @Override
    public final BlockStateContainer getBlockState() {
        return container;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return TileHelper.hasTileEntity(state);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return TileHelper.getTileEntity(state);
    }

    protected BlockStateContainer.Builder createStateContainer() {
        return new BlockStateContainer.Builder(this);
    }

}
