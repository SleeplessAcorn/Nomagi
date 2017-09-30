package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.base.BlockCardinalBase;
import info.sleeplessacorn.nomagi.client.GuiHandler;
import info.sleeplessacorn.nomagi.tile.TilePrivacyLectern;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import javax.annotation.Nullable;

public class BlockPrivacyLectern extends BlockCardinalBase {

    public BlockPrivacyLectern() {
        super("privacy_lectern", Material.WOOD);
        setAABB(new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 0.8125, 0.875));
        setNonFullBlock();
    }

    @Override
    public boolean onBlockActivated(
            World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing,
            float hitX, float hitY, float hitZ) {
        if (world.getTileEntity(pos) != null) {
            int id = GuiHandler.PRIVACY_LECTERN.ordinal();
            FMLNetworkHandler.openGui(player, Nomagi.INSTANCE, id, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePrivacyLectern();
    }

}
