package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.network.MessageOpenCreateRoomGui;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import tehnut.lib.mc.block.BlockAxisY;
import tehnut.lib.mc.model.IModeled;

import java.util.List;
import java.util.Locale;

public class BlockDoorController extends BlockAxisY implements IModeled {

    public static final IProperty<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockDoorController() {
        super(Material.WOOD, PlacementStyle.FACE_AWAY);

        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".door_controller");
        setSoundType(SoundType.WOOD);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta % 4);
        Type type = Type.TENT;
        if (meta >= 4)
            type = Type.BRICK;

        return getDefaultState().withProperty(getProperty(), facing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(getProperty()).getHorizontalIndex();
        if (state.getValue(TYPE) == Type.BRICK)
            meta += 4;

        return meta;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(TYPE).ordinal());
    }

    @Override
    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), TYPE).build();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(TYPE, Type.values()[meta]);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY) {
        if (world.isRemote || hand != EnumHand.MAIN_HAND)
            return true;

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

        Nomagi.NETWORK_WRAPPER.sendTo(new MessageOpenCreateRoomGui(player.chunkCoordX - tent.getChunkX(), player.chunkCoordZ - tent.getChunkZ(), state.getValue(getProperty())), (EntityPlayerMP) player);
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
