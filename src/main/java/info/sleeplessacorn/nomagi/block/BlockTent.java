package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import info.sleeplessacorn.nomagi.ConfigHandler;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Privacy;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.tile.TileEntityTent;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
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
import java.util.Map;

public class BlockTent extends BlockAxisY implements IModeled {

    public static final IProperty<TentType> TENT_TYPE = PropertyEnum.create("tent_type", TentType.class);

    public static final ImmutableMap<EnumFacing, AxisAlignedBB> AABB_TENT = ImmutableMap.copyOf(createTentAABBs());

    public BlockTent() {
        super(Material.CLOTH);
        setCreativeTab(Nomagi.CTAB);
        setUnlocalizedName(Nomagi.ID + ".tent");
        setSoundType(SoundType.CLOTH);
        setHardness(0.8F);
        setResistance(4.0F);
        setDefaultState(getBlockState().getBaseState().withProperty(getProperty(), EnumFacing.NORTH)
                .withProperty(TENT_TYPE, TentType.BASIC));
    }

    @Override
    public IBlockState getStateForPlacement(
            World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(getProperty(), placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityTent tent = (TileEntityTent) world.getTileEntity(pos);
        return state.withProperty(TENT_TYPE, tent.getTentType());
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB_TENT.get(state.getValue(getProperty()));
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(
            World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        int dimId = ModObjects.TENT_DIMENSION.getId();
        if (!world.isRemote && player.isSneaking() && world.provider.getDimension() != dimId) {
            TentWorldSavedData tentData = TentWorldSavedData.getData(world);
            Tent tent = tentData.getTent(player);
            TileEntityTent tileTent = (TileEntityTent) world.getTileEntity(pos);
            if (tileTent != null) {
                if (tileTent.getOwnerId() == null) {
                    tileTent.setOwnerId(player.getGameProfile().getId());
                }
                boolean newTent = false;
                if (tent == null) {
                    // Attempt to enter another player's tent
                    if (!tileTent.getOwnerId().equals(player.getGameProfile().getId())) {
                        tent = tentData.getTent(tileTent.getOwnerId());
                        if (tent == null) return true;
                    } else { // Generate a new tent for player
                        int chunk = tentData.getCount() * ConfigHandler.tentSpacing;
                        Privacy privacy = new Privacy(tileTent.getOwnerId());
                        tent = new Tent(tileTent.getOwnerId(), chunk, 0, privacy);
                        newTent = true;
                        tentData.setTent(player, tent);
                    }
                }
                if (tent.getPrivacy().canEnter(player)) {
                    tentData.sendTo(player, tent); // Teleport to the dimension
                    if (newTent) tent.initialize(player); // Generate the initial tent room
                    return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(
            World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityTent tileTent = (TileEntityTent) world.getTileEntity(pos);
        if ((placer instanceof EntityPlayer) && tileTent != null) {
            tileTent.setOwnerId(((EntityPlayer) placer).getGameProfile().getId());
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("tentType")) {
                String type = stack.getTagCompound().getString("tentType");
                tileTent.setTentType(TentType.valueOf(type.toUpperCase(Locale.ROOT)));
            }
        }
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

    @Override
    public ItemStack getPickBlock(
            IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack stack = new ItemStack(this);
        stack.setTagInfo("tentType", new NBTTagString(state.getValue(TENT_TYPE).getName()));
        return stack;
    }

    @Override
    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty()).add(TENT_TYPE).build();
    }

    @Override
    public void getVariants(List<String> variants) {
        variants.add("inventory");
    }

    private static Map<EnumFacing, AxisAlignedBB> createTentAABBs() {
        AxisAlignedBB aabb = new AxisAlignedBB(-0.50D, 0.00D, 0.00D, 1.50D, 1.75D, 2.00D);
        double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ;
        double maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
        return ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
                .put(EnumFacing.DOWN, new AxisAlignedBB(1 - maxX, minZ, 1 - maxY, 1 - minX, 1, 1 - minY))
                .put(EnumFacing.UP, new AxisAlignedBB(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY))
                .put(EnumFacing.SOUTH, new AxisAlignedBB(1 - maxX, minY, 1 - maxZ, 1 - minX, maxY, 1 - minZ))
                .put(EnumFacing.WEST, new AxisAlignedBB(minZ, minY, minX, maxZ, maxY, maxX))
                .put(EnumFacing.EAST, new AxisAlignedBB(1 - maxZ, minY, 1 - maxX, 1 - minZ, maxY, 1 - minX))
                .build();
    }

    public enum TentType implements IStringSerializable {
        BASIC;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

    }

}
