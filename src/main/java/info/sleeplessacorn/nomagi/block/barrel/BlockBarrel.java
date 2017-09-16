package info.sleeplessacorn.nomagi.block.barrel;

import com.google.common.collect.Lists;
import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tehnut.lib.mc.block.BlockEnum;
import tehnut.lib.mc.model.IModeled;

import java.util.List;
import java.util.Locale;

public class BlockBarrel extends BlockEnum<BlockBarrel.Barrel> implements IModeled {

    private static final AxisAlignedBB BARREL_AABB = new AxisAlignedBB(
            0.1250D, 0.0000D, 0.1250D, 0.8750D, 0.9375D, 0.8750D);

    public BlockBarrel() {
        super(Material.ROCK, BlockBarrel.Barrel.class);
        setUnlocalizedName(Nomagi.MOD_ID + ".barrel");
        setCreativeTab(Nomagi.TAB_NOMAGI);
    }

    public static void playOpeningSound(World world, BlockPos pos) {
        if (!world.isRemote) {
            SoundEvent sound = SoundEvents.ENTITY_ITEMFRAME_PLACE;
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.6f, 0.3f);
        }
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public Material getMaterial(IBlockState state) {
        return state.getValue(getProperty()).getMaterial();
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BARREL_AABB;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(
            World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side,
            float hitX, float hitY, float hitZ) {
        // Nomagi.NET_WRAPPER.sendTo(new MessageOpenBarrelGui(world.getTileEntity(pos), player), (EntityPlayerMP) player);
        // FIXME : Crash from casting EntityPlayerSP to EntityPlayerMP.
        // FIXME : Nut, do stuff pls bcuz I don't understand your system
        playOpeningSound(world, pos);
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrel();
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(new ItemStack(this, 1, getMetaFromState(state) & 3));
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
        return state.getValue(getProperty()).getSound();
    }

    @Override
    public void getVariants(List<String> variants) {
        for (BlockBarrel.Barrel barrels : BlockBarrel.Barrel.values()) {
            variants.add("type=" + barrels.getName());
        }
    }

    public enum Barrel implements IStringSerializable {

        WOODEN(Material.WOOD, SoundType.WOOD),
        METAL(Material.IRON, SoundType.METAL);

        private final Material material;
        private final SoundType sound;

        Barrel(Material material, SoundType sound) {
            this.material = material;
            this.sound = sound;
        }

        public Material getMaterial() {
            return material;
        }

        public SoundType getSound() {
            return sound;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

    }

}