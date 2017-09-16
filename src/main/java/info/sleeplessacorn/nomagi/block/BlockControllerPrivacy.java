package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.network.MessageOpenPrivacyGui;
import info.sleeplessacorn.nomagi.network.MessageUpdateUsernames;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tehnut.lib.mc.block.BlockAxisY;
import tehnut.lib.mc.model.IModeled;

import java.util.List;
import java.util.UUID;

public class BlockControllerPrivacy extends BlockAxisY implements IModeled {

    public BlockControllerPrivacy() {
        super(Material.WOOD);

        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MOD_ID + ".privacy_controller");
        setSoundType(SoundType.WOOD);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
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

        Room room = tent.getRoom(player);
        if (room == null)
            return false;

        MinecraftServer server = world.getMinecraftServer();
        List<UUID> online = Lists.newArrayList();
        for (EntityPlayerMP onlinePlayer : server.getPlayerList().getPlayers())
            online.add(onlinePlayer.getGameProfile().getId());

        Nomagi.NET_WRAPPER.sendTo(new MessageUpdateUsernames(), (EntityPlayerMP) player);
        Nomagi.NET_WRAPPER.sendTo(new MessageOpenPrivacyGui(tent.getPrivacy(), online), (EntityPlayerMP) player);
        return true;
    }

    // blarg
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(getProperty(), placer.getHorizontalFacing().getOpposite());
    }

    // IModeled
    @Override
    public void getVariants(List<String> variants) {
        variants.add("facing=east");
    }
}
