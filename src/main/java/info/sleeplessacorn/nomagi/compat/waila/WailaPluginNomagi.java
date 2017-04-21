package info.sleeplessacorn.nomagi.compat.waila;

import info.sleeplessacorn.nomagi.block.BlockTent;
import info.sleeplessacorn.nomagi.compat.waila.provider.DataProviderNomagi;
import info.sleeplessacorn.nomagi.tile.TileEntityTent;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaPluginNomagi implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(DataProviderNomagi.INSTANCE, BlockTent.class);
        registrar.registerNBTProvider(DataProviderNomagi.INSTANCE, TileEntityTent.class);
    }
}
