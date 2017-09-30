package info.sleeplessacorn.nomagi.compat.waila;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.BlockTent;
import info.sleeplessacorn.nomagi.compat.waila.provider.TentOwnerProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(Nomagi.ID)
public class WailaCompatPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new TentOwnerProvider(), BlockTent.class);
    }
}
