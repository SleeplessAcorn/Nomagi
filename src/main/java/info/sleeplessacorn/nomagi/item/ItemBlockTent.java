package info.sleeplessacorn.nomagi.item;

import info.sleeplessacorn.nomagi.block.BlockTent;
import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

    @Override
    public boolean placeBlockAt(
            ItemStack stack, EntityPlayer player,
            World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ, IBlockState newState) {
        boolean canPlaceMulti = true;
        for (BlockPos pos1 : BlockPos.getAllInBoxMutable(
                pos.subtract(new Vec3i(1, 0, 1)),
                pos.add(new Vec3i(1, 1, 1)))) {
            Block block = world.getBlockState(pos1).getBlock();
            if (block != Blocks.AIR) canPlaceMulti = false;
        }
        return canPlaceMulti && super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
    }
}
