package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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

public class BlockController extends BlockEnum<BlockController.Controller> implements IModeled {

    public BlockController() {
        super(Material.ROCK, Controller.class);

        setUnlocalizedName(Nomagi.MODID + ".controller");
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setSoundType(SoundType.STONE);
    }

    @Override
    public boolean onBlockActivated(
            World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem,
            float side, float hitX, float hitY) {
        Nomagi.PROXY.displayRoomControllerGui();
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        if (getProperty() == null)
            return super.isOpaqueCube(state); // Fuck you Mojang
        else return state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return state.getValue(getProperty()).getHitBox() == FULL_BLOCK_AABB;
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
    public void getVariants(List<String> variants) {
        for (BlockController.Controller nature : BlockController.Controller.values())
            variants.add("type=" + nature.getName());
    }

    public enum Controller implements IStringSerializable {
        TENT(Material.CLOTH, SoundType.CLOTH),
        BRICK(Material.ROCK, SoundType.STONE),
        ;

        private final AxisAlignedBB hitBox;
        private final Pair<Material, SoundType> blockType;

        Controller(Material material, SoundType soundType) {
            this(FULL_BLOCK_AABB, material, soundType);
        }

        Controller(AxisAlignedBB bounds, Material material, SoundType soundType) {
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
