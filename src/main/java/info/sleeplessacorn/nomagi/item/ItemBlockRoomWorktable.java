package info.sleeplessacorn.nomagi.item;

import info.sleeplessacorn.nomagi.block.BlockRoomWorktable;
import info.sleeplessacorn.nomagi.block.BlockRoomWorktable.Side;
import info.sleeplessacorn.nomagi.block.base.BlockEnumCardinalBase;
import info.sleeplessacorn.nomagi.core.RegistrarNomagi;
import info.sleeplessacorn.nomagi.item.base.ItemBlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockRoomWorktable extends ItemBlockBase {

    public ItemBlockRoomWorktable(BlockRoomWorktable block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(
            ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
            float hitZ, IBlockState newState) {
        BlockPos posOffsetLeft = pos.offset(player.getHorizontalFacing().rotateYCCW());
        BlockPos posOffsetRight = pos.offset(player.getHorizontalFacing().rotateY());
        if (world.getBlockState(posOffsetRight).getBlock().isReplaceable(world, posOffsetRight)) {
            return placeWorktableAt(world, player, pos, posOffsetRight);
        } else {
            if (world.getBlockState(posOffsetLeft).getBlock().isReplaceable(world, posOffsetLeft)) {
                return placeWorktableAt(world, player, posOffsetLeft, pos);
            }
        }
        return false;
    }

    private boolean placeWorktableAt(World world, EntityPlayer player, BlockPos posLeft, BlockPos posRight) {
        if (!world.isRemote) {
            BlockRoomWorktable block = (BlockRoomWorktable) RegistrarNomagi.ROOM_WORKTABLE;
            PropertyDirection direction = BlockEnumCardinalBase.getFacingProperty();
            EnumFacing facing = player.getHorizontalFacing().getOpposite();
            IBlockState state = block.getDefaultState().withProperty(direction, facing);
            SoundType sound = SoundType.WOOD;
            world.playSound(null, posLeft, sound.getPlaceSound(), SoundCategory.BLOCKS,
                    (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            return world.setBlockState(posLeft, state.withProperty(block.getPropertyEnum(), Side.LEFT)) &&
                    world.setBlockState(posRight, state.withProperty(block.getPropertyEnum(), Side.RIGHT));
        }
        return false;
    }

}
