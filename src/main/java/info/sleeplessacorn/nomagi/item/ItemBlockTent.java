package info.sleeplessacorn.nomagi.item;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.item.ItemBlock;

public class ItemBlockTent extends ItemBlock {

    public ItemBlockTent() {
        super(ModObjects.TENT);
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setUnlocalizedName(Nomagi.MODID + ".tent");
    }
}
