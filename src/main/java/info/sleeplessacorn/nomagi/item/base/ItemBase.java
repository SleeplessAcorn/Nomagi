package info.sleeplessacorn.nomagi.item.base;

import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.client.IModelProvider;
import info.sleeplessacorn.nomagi.client.WrappedModel;
import info.sleeplessacorn.nomagi.util.StringHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public class ItemBase extends Item implements IModelProvider {

    public ItemBase(String name) {
        setUnlocalizedName(Nomagi.ID + "." + StringHelper.toLangKey(name));
        setCreativeTab(Nomagi.TAB);
    }

    @Override
    public void gatherModels(Set<WrappedModel> models) {
        models.add(new WrappedModel.Builder(this).build());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getUnlocalizedName() + ".tooltip";
        if (I18n.hasKey(key))
            tooltip.add(I18n.format(key));
    }
}
