package info.sleeplessacorn.nomagi.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float x, float y, float z) {
        pos = isReplaceable(world, pos) ? pos : pos.offset(facing);
        boolean placeLower = !isReplaceable(world, pos.down());
        boolean isYAxis = facing.getAxis().equals(EnumFacing.Axis.Y);
        EnumFacing actualFacing = isYAxis ? player.getHorizontalFacing().getOpposite() : facing;
        BlockPos pos1 = placeLower ? pos.up() : pos.down();

        if (canPlace(world, pos, placeLower, actualFacing)) {
            if (placeTapestry(world, pos, pos1, actualFacing, x, y, z, player, hand, placeLower)) {
                SoundType sound = getBlock().getSoundType(getBlock().getDefaultState(), world, pos, player);
                world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
                player.getHeldItem(hand).shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.FAIL;
    }

    private boolean isReplaceable(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    private boolean canPlace(World world, BlockPos pos, boolean isLower, EnumFacing facing) {
        BlockPos posAt = pos.offset(facing.getOpposite());
        return world.isSideSolid(isLower ? posAt.up() : posAt, facing);
    }

    private boolean placeTapestry(World world, BlockPos pos, BlockPos pos1, EnumFacing facing, float x, float y, float z, EntityPlayer player, EnumHand hand, boolean placeLower) {
        int meta0 = placeLower ? 0 : 1;
        int meta1 = placeLower ? 1 : 0;
        IBlockState state0 = getBlock().getStateForPlacement(world, pos, facing, x, y, z, meta0, player, hand);
        IBlockState state1 = getBlock().getStateForPlacement(world, pos, facing, x, y, z, meta1, player, hand);
        return placeBlockAt(player.getHeldItem(hand), player, world, pos, facing, x, y, z, state0) && placeBlockAt(player.getHeldItem(hand), player, world, pos1, facing, x, y, z, state1);
    }
}
