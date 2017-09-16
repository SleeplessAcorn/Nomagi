package info.sleeplessacorn.nomagi.compat.waila.provider;

import com.google.common.base.Strings;
import info.sleeplessacorn.nomagi.core.ModObjects;
import info.sleeplessacorn.nomagi.tile.TileEntityTent;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;

import java.util.List;

public class DataProviderNomagi implements IWailaDataProvider {

    public static final IWailaDataProvider INSTANCE = new DataProviderNomagi();

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return accessor.getStack();
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() == ModObjects.TENT) {
            String owner = accessor.getNBTData().getString("ownerName");
            currenttip.add(new TextComponentTranslation("info.nomagi.owner", owner).getUnformattedText());
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (te instanceof TileEntityTent) {
            TileEntityTent tent = (TileEntityTent) te;
            String displayName = UsernameCache.getLastKnownUsername(tent.getOwnerId());
            tag.setString("ownerName", Strings.isNullOrEmpty(displayName) ? "< Unknown >" : displayName);
        }
        return tag;
    }
}
