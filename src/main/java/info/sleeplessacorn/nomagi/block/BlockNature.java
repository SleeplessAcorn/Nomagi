package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tehnut.lib.mc.block.BlockEnum;
import tehnut.lib.mc.model.IModeled;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockNature extends BlockEnum<BlockNature.Nature> implements IModeled {

    public BlockNature() {
        super(Material.ROCK, Nature.class);
        setUnlocalizedName(Nomagi.ID + ".nature");
        setCreativeTab(Nomagi.CTAB);
        setSoundType(SoundType.WOOD);
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return state.getValue(getProperty()).isFullCube();
    }

    @Override
    public int getLightOpacity(IBlockState state) {
        return state.getValue(getProperty()).getLightOpacity();
    }

    @Override
    @Deprecated
    public Material getMaterial(IBlockState state) {
        return state.getValue(getProperty()).getMaterial();
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return state.getValue(getProperty()).isFullCube();
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(getProperty()).getBoundingBox();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return getProperty() != null ? state.getValue(getProperty()).isFullCube() : super.isOpaqueCube(state);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(getProperty()).getSound();
    }

    @Override
    public void getVariants(List<String> variants) {
        for (BlockNature.Nature nature : BlockNature.Nature.values()) {
            variants.add("type=" + nature.getName());
        }
    }

    public enum Nature implements IStringSerializable {

        RADIANT_GRASS(Material.GROUND, SoundType.GROUND),
        ANCIENT_BARK(Material.WOOD, SoundType.WOOD),
        ANCIENT_PLANKS(Material.WOOD, SoundType.WOOD),
        ANCIENT_LEAVES(Material.LEAVES, SoundType.PLANT) {
            @Override
            public boolean isFullCube() {
                return false;
            }
        };

        private final AxisAlignedBB aabb;
        private final Material material;
        private final SoundType sound;

        Nature(Material material, SoundType sound) {
            this(FULL_BLOCK_AABB, material, sound);
        }

        Nature(AxisAlignedBB aabb, Material material, SoundType sound) {
            this.aabb = aabb;
            this.material = material;
            this.sound = sound;
        }

        public AxisAlignedBB getBoundingBox() {
            return aabb;
        }

        public Material getMaterial() {
            return material;
        }

        public SoundType getSound() {
            return sound;
        }

        public boolean isFullCube() {
            return aabb == FULL_BLOCK_AABB;
        }

        public int getLightOpacity() {
            return equals(ANCIENT_LEAVES) ? 1 : 255;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

    }

}
