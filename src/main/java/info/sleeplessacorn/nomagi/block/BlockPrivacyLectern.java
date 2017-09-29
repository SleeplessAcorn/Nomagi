package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.ModGuis;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.base.BlockCardinalBase;
import info.sleeplessacorn.nomagi.tile.TilePrivacyLectern;
import info.sleeplessacorn.nomagi.util.ITileHolder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import javax.annotation.Nullable;

public class BlockPrivacyLectern extends BlockCardinalBase implements ITileHolder {

    public BlockPrivacyLectern() {
        super("privacy_lectern", Material.WOOD);
        setAABB(new AxisAlignedBB(0.125, 0.0, 0.125, 0.875, 0.8125, 0.875));
        setNonFullBlock();
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(IBlockState state) {
        return new TilePrivacyLectern();
    }

    @Override
    public boolean onTileInteract(IBlockState state, World world, BlockPos pos, EntityPlayer player) {
        if (world.getTileEntity(pos) != null) {
            int id = ModGuis.PRIVACY_LECTERN.getID();
            FMLNetworkHandler.openGui(player, Nomagi.instance, id, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

}
