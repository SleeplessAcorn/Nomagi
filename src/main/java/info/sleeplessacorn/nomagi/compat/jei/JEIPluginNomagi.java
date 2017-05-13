package info.sleeplessacorn.nomagi.compat.jei;

import info.sleeplessacorn.nomagi.core.ModObjects;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.Item;

@JEIPlugin
public class JEIPluginNomagi extends BlankModPlugin {

    @Override
    public void register(IModRegistry registry) {

    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(Item.getItemFromBlock(ModObjects.TENT));
    }
}
