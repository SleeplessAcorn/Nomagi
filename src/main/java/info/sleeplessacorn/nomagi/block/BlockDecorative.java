package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tehnut.lib.mc.block.BlockEnum;
import tehnut.lib.mc.model.IModeled;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockDecorative extends BlockEnum<BlockDecorative.Decor> implements IModeled {

    public BlockDecorative() {
        super(Material.ROCK, Decor.class);
        setUnlocalizedName(Nomagi.ID + ".decor");
        setCreativeTab(Nomagi.CTAB);
        setSoundType(SoundType.STONE);
    }

    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return state.getValue(getProperty()).isFullCube();
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
    public boolean isOpaqueCube(IBlockState state) {
        return getProperty() != null ? state.getValue(getProperty()).isFullCube() : super.isOpaqueCube(state);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(getProperty()).getSound();
    }

    @Override
    public void getVariants(List<String> variants) {
        for (Decor decor : Decor.values()) {
            variants.add("type=" + decor.getName());
        }
    }

    public enum Decor implements IStringSerializable {

        UMBERSTONE(Material.ROCK, SoundType.STONE),
        EBONSTONE(Material.ROCK, SoundType.STONE),
        TENT_WALL(Material.CLOTH, SoundType.CLOTH),
        TENT_WALL_RIBBON(Material.CLOTH, SoundType.CLOTH),
        TENT_WALL_INSET(Material.CLOTH, SoundType.CLOTH),
        EBONSTONE_INSET(Material.ROCK, SoundType.STONE);

        private final AxisAlignedBB aabb;
        private final Material material;
        private final SoundType sound;

        Decor(Material material, SoundType sound) {
            this(FULL_BLOCK_AABB, material, sound);
        }

        Decor(AxisAlignedBB aabb, Material material, SoundType sound) {
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

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

    }

}
