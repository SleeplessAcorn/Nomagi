package info.sleeplessacorn.nomagi.block.barrel;

/*
 * This file was created at 17:45 on 20 Apr 2017 by InsomniaKitten
 *
 * It is distributed as part of the DimensionallyTranscendentalTents mod.
 * Source code is visible at: https://github.com/InsomniaKitten/DimensionallyTranscendentalTents
 *
 * Copyright (c) InsomniaKitten 2017. All Rights Reserved.
 */

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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import tehnut.lib.mc.block.BlockEnum;
import tehnut.lib.mc.model.IModeled;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockBarrel extends BlockEnum<BlockBarrel.Barrel> implements IModeled {

    private static final AxisAlignedBB BARREL_AABB = new AxisAlignedBB(0.125D, 0D, 0.125D, 0.875D, 0.9375D, 0.875D);

    public BlockBarrel() {
        super(Material.ROCK, BlockBarrel.Barrel.class);

        setUnlocalizedName(Nomagi.MODID + ".barrel");
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setSoundType(SoundType.STONE);
    }

    @Override public boolean isFullBlock(IBlockState state) { return false; }
    @Override public boolean isFullCube(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(getProperty()).getHitBox();
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return state.getValue(getProperty()).getBlockType().getLeft();
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(getProperty()).getBlockType().getRight();
    }

    public static void playOpeningSound(World world, BlockPos pos){
        if (!world.isRemote)
            world.playSound(
                    null, pos, SoundEvents.ENTITY_ITEMFRAME_PLACE,
                    SoundCategory.BLOCKS, 0.6f, 0.3f
            );
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(
            World worldIn, EntityPlayer player,
            BlockPos pos, IBlockState state,
            @Nullable TileEntity te, @Nullable ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
        if (te instanceof TileBarrel)
            te.invalidate();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileBarrel();
    }


    @Override
    @Nonnull
    public java.util.List<ItemStack> getDrops(
            IBlockAccess world, BlockPos pos,
            @Nonnull IBlockState state, int fortune) {
        ItemStack stack = new ItemStack(
                this, 1, getMetaFromState(state) & 3);
        return Lists.newArrayList(stack);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, EnumFacing heldItem,
                                    float side, float hitX, float hitY) {
        if (!world.isRemote) {
            playOpeningSound(world, pos);
            player.openGui(Nomagi.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void getVariants(List<String> variants) {
        for (BlockBarrel.Barrel barrels : BlockBarrel.Barrel.values())
            variants.add("type=" + barrels.getName());
    }

    public enum Barrel implements IStringSerializable {
        WOODEN_BARREL(BARREL_AABB, Material.WOOD, SoundType.WOOD),
        METAL_BARREL(BARREL_AABB, Material.IRON, SoundType.METAL),
        ;

        private final AxisAlignedBB hitBox;
        private final Pair<Material, SoundType> blockType;

        Barrel(Material material, SoundType soundType) {
            this(BARREL_AABB, material, soundType);
        }

        Barrel(AxisAlignedBB bounds, Material material, SoundType soundType) {
            this.hitBox = bounds;
            this.blockType = Pair.of(material, soundType);
        }

        public AxisAlignedBB getHitBox() {
            return hitBox;
        }

        public Pair<Material, SoundType> getBlockType() {
            return blockType;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

}