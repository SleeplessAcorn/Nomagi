package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.data.Tent;
import info.sleeplessacorn.nomagi.core.data.TentWorldSavedData;
import info.sleeplessacorn.nomagi.tile.TileEntityTent;
import info.sleeplessacorn.nomagi.world.TeleporterTent;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTent extends Block {

    public BlockTent() {
        super(Material.CLOTH);
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".tent");
        setSoundType(SoundType.CLOTH);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote || !player.isSneaking())
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
                tent = new Tent(tileEntityTent.getOwnerId(), tentData.getCount() * 16, 0);
                newTent = true;
                tentData.setTent(player, tent);
            }
        }

        tentData.sendTo(player, tent); // Teleport to the dimension
        if (newTent)
            tent.initialize(player); // Generate the initial tent room
        return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (!(placer instanceof EntityPlayer))
            return;

        ((TileEntityTent) world.getTileEntity(pos)).setOwnerId(((EntityPlayer) placer).getGameProfile().getId());
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
    public AxisAlignedBB getBoundingBox(
            IBlockState state, IBlockAccess source, BlockPos pos) {
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
}
