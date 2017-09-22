package info.sleeplessacorn.nomagi.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemNomagiDoor extends ItemBlock {

    public ItemNomagiDoor(Block block) {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;

        IBlockState state = world.getBlockState(pos);
        ItemStack held = player.getHeldItem(hand);

        if (!state.getBlock().isReplaceable(world, pos)) {
            pos = pos.offset(facing);
            state = world.getBlockState(pos);
        }

        if (player.canPlayerEdit(pos, facing, held) && block.canPlaceBlockAt(world, pos)) {
            EnumFacing enumfacing = EnumFacing.fromAngle((double) player.rotationYaw);
            int frontOffsetX = enumfacing.getFrontOffsetX();
            int frontOffsetZ = enumfacing.getFrontOffsetZ();
            boolean isRightHinge = frontOffsetX < 0 && hitZ < 0.5F || frontOffsetX > 0 && hitZ > 0.5F || frontOffsetZ < 0 && hitX > 0.5F || frontOffsetZ > 0 && hitX < 0.5F;

            ItemDoor.placeDoor(world, pos, enumfacing, block, isRightHinge);
            SoundType soundtype = state.getBlock().getSoundType(state, world, pos, player);
            world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            held.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
