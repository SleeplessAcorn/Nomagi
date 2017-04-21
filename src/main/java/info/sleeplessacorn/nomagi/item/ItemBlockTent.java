package info.sleeplessacorn.nomagi.item;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.item.ItemBlock;
import tehnut.lib.mc.model.IModeled;

import java.util.List;

public class ItemBlockTent extends ItemBlock implements IModeled {

    public ItemBlockTent() {
        super(ModObjects.TENT);
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".tent");
    }

    @Override
    public void getVariants(List<String> variants) {
        variants.add("inventory");
    }

}
