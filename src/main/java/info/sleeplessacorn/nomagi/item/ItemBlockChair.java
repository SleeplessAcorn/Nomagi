package info.sleeplessacorn.nomagi.item;

import info.sleeplessacorn.nomagi.block.BlockChair;
import info.sleeplessacorn.nomagi.block.BlockChair.Half;
import info.sleeplessacorn.nomagi.block.base.BlockEnumCardinalBase;
import info.sleeplessacorn.nomagi.core.RegistrarNomagi;
import info.sleeplessacorn.nomagi.item.base.ItemBlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockChair extends ItemBlockBase {

    public ItemBlockChair(BlockChair block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()))
            return placeChairAt(world, player, pos.up(), pos);
        else if (world.getBlockState(pos.down()).getBlock().isReplaceable(world, pos.down()))
            return placeChairAt(world, player, pos, pos.down());

        return false;
    }

    private boolean placeChairAt(World world, EntityPlayer player, BlockPos posUpper, BlockPos posLower) {
        if (!world.isRemote) {
            BlockChair block = (BlockChair) RegistrarNomagi.CHAIR;
            EnumFacing facing = player.getHorizontalFacing().getOpposite();
            IBlockState state = block.getDefaultState().withProperty(BlockEnumCardinalBase.FACING, facing);
            SoundType sound = SoundType.WOOD;
            world.playSound(null, posLower, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
            return world.setBlockState(posUpper, state.withProperty(block.getPropertyEnum(), Half.UPPER)) && world.setBlockState(posLower, state.withProperty(block.getPropertyEnum(), Half.LOWER));
        }

        return false;
    }
}
