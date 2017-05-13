package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import tehnut.lib.mc.block.BlockEnum;
import tehnut.lib.mc.model.IModeled;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class BlockNature extends BlockEnum<BlockNature.Nature> implements IModeled {

    public BlockNature() {
        super(Material.ROCK, Nature.class);

        setUnlocalizedName(Nomagi.MODID + ".nature");
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setSoundType(SoundType.WOOD);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = world.getBlockState(pos.offset(side));
        return state != iblockstate || state.getValue(getProperty()) != Nature.ANCIENT_LEAVES;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        if (getProperty() == null)
            return super.isOpaqueCube(state); // Fuck you Mojang

        return state.getValue(getProperty()) != Nature.ANCIENT_LEAVES && state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(getProperty()) != Nature.ANCIENT_LEAVES && state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return state.getValue(getProperty()) != Nature.ANCIENT_LEAVES && state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void getVariants(List<String> variants) {
        for (BlockNature.Nature nature : BlockNature.Nature.values())
            variants.add("type=" + nature.getName());
    }

    public enum Nature implements IStringSerializable {
        RADIANT_GRASS(Material.GROUND, SoundType.GROUND),
        ANCIENT_BARK(Material.WOOD, SoundType.WOOD),
        ANCIENT_PLANKS(Material.WOOD, SoundType.WOOD),
        ANCIENT_LEAVES(Material.LEAVES, SoundType.PLANT),
        ;

        private final AxisAlignedBB hitBox;
        private final Pair<Material, SoundType> blockType;

        Nature(Material material, SoundType soundType) {
            this(FULL_BLOCK_AABB, material, soundType);
        }

        Nature(AxisAlignedBB bounds, Material material, SoundType soundType) {
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
