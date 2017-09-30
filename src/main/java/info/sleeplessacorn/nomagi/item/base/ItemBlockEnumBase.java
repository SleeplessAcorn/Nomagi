package info.sleeplessacorn.nomagi.item.base;

import info.sleeplessacorn.nomagi.block.base.BlockEnumBase;
import info.sleeplessacorn.nomagi.block.base.IPropertyProvider;
import info.sleeplessacorn.nomagi.client.WrappedModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

public class ItemBlockEnumBase<E extends Enum<E> & IPropertyProvider<E>> extends ItemBlockBase {

    protected final E[] values;

    public ItemBlockEnumBase(BlockEnumBase<E> block) {
        super(block);
        this.values = block.getValues();
        setHasSubtypes(true);
    }

    @Override
    public void getModels(Set<WrappedModel> models) {
        for (E value : values) {
            WrappedModel.Builder model = new WrappedModel.Builder(this, value.getMetadata());
            model.addVariant("type=" + value.getName());
            models.add(model.build());
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getMetadata() % values.length;
        return this.getUnlocalizedName() + "." + values[meta].getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getUnlocalizedName() + ".tooltip";
        if (I18n.hasKey(key))
            tooltip.add(I18n.format(key));
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}

