package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.ConfigHandler;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Privacy;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.tile.TileEntityTent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tehnut.lib.mc.block.BlockAxisY;
import tehnut.lib.mc.model.IModeled;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockTent extends BlockAxisY implements IModeled {

    public static final IProperty<TentType> TENT_TYPE = PropertyEnum.create("tent_type", TentType.class);

    public BlockTent() {
        super(Material.CLOTH);
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".tent");
        setSoundType(SoundType.CLOTH);

        setDefaultState(getBlockState().getBaseState().withProperty(getProperty(), EnumFacing.NORTH).withProperty(TENT_TYPE, TentType.BASIC));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote || !player.isSneaking())
            return false;

        if (world.provider.getDimension() == ModObjects.TENT_DIMENSION.getId())
            return false;

        TentWorldSavedData tentData = TentWorldSavedData.getData(world);
        Tent tent = tentData.getTent(player);
        TileEntityTent tileEntityTent = (TileEntityTent) world.getTileEntity(pos);
        if (tileEntityTent.getOwnerId() == null)
            tileEntityTent.setOwnerId(player.getGameProfile().getId());

        boolean newTent = false;
        if (tent == null) {
            if (!tileEntityTent.getOwnerId().equals(player.getGameProfile().getId())) { // Attempt to enter another player's tent
                tent = tentData.getTent(tileEntityTent.getOwnerId());
                if (tent == null)
                    return true;
            } else { // Generate a new tent for player
                tent = new Tent(tileEntityTent.getOwnerId(), tentData.getCount() * ConfigHandler.tentSpacing, 0, new Privacy(tileEntityTent.getOwnerId()));
                newTent = true;
                tentData.setTent(player, tent);
            }
        }

        if (!tent.getPrivacy().canEnter(player))
            return false;

        tentData.sendTo(player, tent); // Teleport to the dimension
        if (newTent)
            tent.initialize(player); // Generate the initial tent room
        return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!(placer instanceof EntityPlayer))
            return;

        TileEntityTent tent = (TileEntityTent) world.getTileEntity(pos);

        tent.setOwnerId(((EntityPlayer) placer).getGameProfile().getId());
        if (!stack.hasTagCompound())
            return;

        tent.setTentType(TentType.valueOf(stack.getTagCompound().getString("tentType").toUpperCase(Locale.ENGLISH)));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(getProperty(), placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("tentType", state.getValue(TENT_TYPE).getName());
        return stack;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityTent tent = (TileEntityTent) world.getTileEntity(pos);
        return state.withProperty(TENT_TYPE, tent.getTentType());
    }

    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), TENT_TYPE).build();
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(-0.5D, 0D, 0D, 1.5D, 1.75D, 2D);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTent();
    }

    // IModeled
    @Override
    public void getVariants(List<String> variants) {
        variants.add("inventory");
    }

    public enum TentType implements IStringSerializable {
        BASIC,
        ;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
