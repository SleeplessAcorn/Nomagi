package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import tehnut.lib.mc.block.BlockEnum;
import tehnut.lib.mc.model.IModeled;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockDecorative extends BlockEnum<BlockDecorative.Decor> implements IModeled {

    public BlockDecorative() {
        super(Material.ROCK, Decor.class);

        setUnlocalizedName(Nomagi.MODID + ".decor");
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setSoundType(SoundType.STONE);
    }

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

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        if (getProperty() == null)
            return super.isOpaqueCube(state); // Fuck you Mojang
        return state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
    }

    // IModeled

    @Override
    public void getVariants(List<String> variants) {
        for (Decor decor : Decor.values())
            variants.add("type=" + decor.getName());
    }

    public enum Decor implements IStringSerializable {
        UMBERSTONE(Material.ROCK, SoundType.STONE),
        EBONSTONE(Material.ROCK, SoundType.STONE),
        TENT_WALL(Material.CLOTH, SoundType.CLOTH),
        TENT_WALL_RIBBON(Material.CLOTH, SoundType.CLOTH),
        ;

        private final AxisAlignedBB hitBox;
        private final Pair<Material, SoundType> blockType;

        Decor(Material material, SoundType soundType) {
            this(FULL_BLOCK_AABB, material, soundType);
        }

        Decor(AxisAlignedBB bounds, Material material, SoundType soundType) {
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
