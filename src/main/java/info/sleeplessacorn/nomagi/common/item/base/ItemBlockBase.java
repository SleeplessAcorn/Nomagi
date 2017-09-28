package info.sleeplessacorn.nomagi.common.item.base;

import info.sleeplessacorn.nomagi.client.model.ModelRegistry;
import info.sleeplessacorn.nomagi.client.model.WrappedModel;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockBase extends ItemBlock {

    public ItemBlockBase(Block block) {
        super(block);
        //noinspection ConstantConditions
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getUnlocalizedName());
        setCreativeTab(block.getCreativeTabToDisplayOn());
        registerModels();
    }

    protected void registerModels() {
        ModelRegistry.registerModel(new WrappedModel.Builder(this).addVariant("normal").build());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        String key = stack.getUnlocalizedName() + ".tooltip";
        if (I18n.hasKey(key)) {
            tooltip.add(I18n.format(key));
        }
    }

}
