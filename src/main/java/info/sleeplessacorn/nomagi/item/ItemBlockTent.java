package info.sleeplessacorn.nomagi.item;

import info.sleeplessacorn.nomagi.block.BlockTent;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class ItemBlockTent extends ItemBlockMulti {

    public ItemBlockTent() {
        super(ModObjects.TENT);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (BlockTent.TentType tentType : BlockTent.TentType.values()) {
            ItemStack stack = new ItemStack(itemIn);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("tentType", tentType.getName());
            subItems.add(stack);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (!stack.hasTagCompound())
            return super.getUnlocalizedName(stack);

        return super.getUnlocalizedName(stack) + "." + stack.getTagCompound().getString("tentType");
    }
}
