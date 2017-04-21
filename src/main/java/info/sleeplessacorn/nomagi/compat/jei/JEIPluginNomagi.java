package info.sleeplessacorn.nomagi.compat.jei;

import info.sleeplessacorn.nomagi.block.BlockDecorative;
import info.sleeplessacorn.nomagi.core.ModObjects;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIPluginNomagi extends BlankModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(ModObjects.DECOR, 1, BlockDecorative.Decor.SOMETHING_SPECIAL.ordinal()));
    }
}
