package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.network.MessageOpenCreateRoomGui;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tehnut.lib.mc.model.IModeled;

import java.util.List;
import java.util.Locale;

public class BlockDoorController extends Block implements IModeled {

    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final IProperty<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockDoorController() {
        super(Material.WOOD);

        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".door_controller");
        setSoundType(SoundType.WOOD);

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.HORIZONTALS[meta % 4];
        Type type = Type.TENT;
        if (meta >= 4)
            type = Type.BRICK;

        return getDefaultState().withProperty(FACING, facing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case NORTH: meta = 0; break;
            case EAST: meta = 1; break;
            case SOUTH: meta = 2; break;
            case WEST: meta = 3; break;
        }
        if (state.getValue(TYPE) == Type.BRICK)
            meta += 4;

        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(FACING, TYPE).build();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(TYPE, Type.values()[meta]);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        if (world.isRemote || hand != EnumHand.MAIN_HAND)
            return false;

        TentWorldSavedData tentData = TentWorldSavedData.getData(world);
        Tent tent = tentData.getTent(player.chunkCoordX, player.chunkCoordZ);
        if (tent == null)
            return false;

        if (!tent.isInside(player))
            return false;

        if (!tent.getOwnerId().equals(player.getGameProfile().getId()))
            return false;

        if (player.isSneaking() && world.provider.getDimension() == ModObjects.TENT_DIMENSION.getId()) {
            tentData.sendBack(player);
            return false;
        }

        Room room = tent.getRoom(player);
        if (room == null)
            return false;

        Nomagi.NETWORK_WRAPPER.sendTo(new MessageOpenCreateRoomGui(player.chunkCoordX - tent.getChunkX(), player.chunkCoordZ - tent.getChunkZ(), state.getValue(FACING)), (EntityPlayerMP) player);
        return true;
    }

    @Override
    public void getVariants(List<String> variants) {
        variants.add("facing=east,type=tent");
        variants.add("facing=east,type=brick");
    }

    public enum Type implements IStringSerializable {
        TENT,
        BRICK,
        ;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
