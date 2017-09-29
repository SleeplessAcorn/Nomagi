package info.sleeplessacorn.nomagi.compat.waila.provider;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class TentOwnerProvider implements IWailaDataProvider {

    @Override
    @Nonnull
    public List<String> getWailaBody(
            ItemStack stack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return tooltip; // TODO
    }

}
