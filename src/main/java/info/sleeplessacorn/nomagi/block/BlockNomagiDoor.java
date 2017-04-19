package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tehnut.lib.mc.model.IModeled;

import java.util.List;
import java.util.Random;

// TODO - Move to LendingLibrary
public class BlockNomagiDoor extends BlockDoor implements IModeled {

    public BlockNomagiDoor() {
        super(Material.WOOD);

        setUnlocalizedName(Nomagi.MODID + ".door");
        setSoundType(SoundType.WOOD);
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setHardness(3.0F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.field_190931_a : getDoorItem().getItem();
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return getDoorItem();
    }

    private ItemStack getDoorItem() {
        return new ItemStack(Item.getItemFromBlock(this));
    }

    @Override
    public void getVariants(List<String> variants) {
        variants.add("inventory");
    }
}
