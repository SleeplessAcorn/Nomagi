package info.sleeplessacorn.nomagi.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockTapestry extends ItemBlock {

    public ItemBlockTapestry(Block block) {
        super(block);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState topState = world.getBlockState(pos);
        if (!topState.getBlock().isReplaceable(world, pos))
            pos = pos.offset(facing);

        if (!topState.isSideSolid(world, pos, facing))
            return EnumActionResult.FAIL;

        ItemStack heldItem = player.getHeldItem(hand);

        if (heldItem.isEmpty())
            return EnumActionResult.FAIL;

        if (!player.canPlayerEdit(pos, facing, heldItem) || !player.canPlayerEdit(pos.down(), facing, heldItem))
            return EnumActionResult.FAIL;

        if (!world.mayPlace(block, pos, false, facing, player) || !world.mayPlace(block, pos.down(), false, facing, player))
            return EnumActionResult.FAIL;

        IBlockState topPlace = block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 1, player, hand);
        IBlockState bottomPlace = block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player, hand);

        if (placeBlockAt(heldItem, player, world, pos, facing, hitX, hitY, hitZ, topPlace) && placeBlockAt(heldItem, player, world, pos.down(), facing, hitX, hitY, hitZ, bottomPlace)) {
            SoundType soundtype = topPlace.getBlock().getSoundType(topPlace, world, pos, player);
            world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            heldItem.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}
