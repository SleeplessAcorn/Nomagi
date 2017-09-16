package info.sleeplessacorn.nomagi.item;

import info.sleeplessacorn.nomagi.block.BlockTent;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class ItemBlockTent extends ItemBlockMulti {

    public ItemBlockTent() {
        super(ModObjects.TENT);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.hasTagCompound()
               ? super.getUnlocalizedName(stack) + "." + stack.getTagCompound().getString("tentType")
               : super.getUnlocalizedName(stack);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (BlockTent.TentType tentType : BlockTent.TentType.values()) {
            ItemStack stack = new ItemStack(itemIn);
            stack.setTagInfo("tentType", new NBTTagString(tentType.getName()));
            subItems.add(stack);
        }
    }

    @Override
    public boolean placeBlockAt(
            ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ, IBlockState state) {
        boolean canPlaceTent = true;
        Vec3i min = new Vec3i(1, 0, 1), max = new Vec3i(1, 1, 1);
        for (BlockPos.MutableBlockPos posAt : BlockPos.getAllInBoxMutable(pos.subtract(min), pos.add(max))) {
            if (!world.getBlockState(posAt).getBlock().isReplaceable(world, posAt)) {
                canPlaceTent = false;
            }
        }
        return canPlaceTent && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
    }

}
