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

    public EnumActionResult onItemUse(
            EntityPlayer player, World world, BlockPos pos, EnumHand hand,
            EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState stateAt = world.getBlockState(pos);

        if (!stateAt.getBlock().isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }

        if (!stateAt.isSideSolid(world, pos, facing) || !canPlaceAt(world, pos, facing, player, hand)) {
            return EnumActionResult.FAIL;
        }

        if (placeTapestryAt(player, hand, world, pos, facing, hitX, hitY, hitZ)) {
            world.playSound(player, pos, SoundType.WOOD.getPlaceSound(), SoundCategory.BLOCKS,
                    (SoundType.WOOD.getVolume() + 1.0F) / 2.0F, SoundType.WOOD.getPitch() * 0.8F);
            player.getHeldItem(hand).shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

    private boolean placeTapestryAt(
            EntityPlayer player, EnumHand hand, World world, BlockPos pos,
            EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState topPlace = block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 1, player, hand);
        IBlockState bottomPlace = block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player, hand);
        ItemStack heldItem = player.getHeldItem(hand);
        return  placeBlockAt(heldItem, player, world, pos, facing, hitX, hitY, hitZ, topPlace)
                && placeBlockAt(heldItem, player, world, pos.down(), facing, hitX, hitY, hitZ, bottomPlace);
    }

    private boolean canPlaceAt(World world, BlockPos pos, EnumFacing facing, EntityPlayer player, EnumHand hand) {
        return  !player.getHeldItem(hand).isEmpty()
                && player.canPlayerEdit(pos, facing, player.getHeldItem(hand))
                && player.canPlayerEdit(pos.down(), facing, player.getHeldItem(hand))
                && world.mayPlace(block, pos, false, facing, player)
                && world.mayPlace(block, pos.down(), false, facing, player);
    }

}
