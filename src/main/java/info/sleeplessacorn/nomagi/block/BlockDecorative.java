package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.Nomagi;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
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

public class BlockDecorative extends BlockEnum<BlockDecorative.Decor> implements IModeled {

    private static final AxisAlignedBB PILLAR_AABB = new AxisAlignedBB(-0.5D, 0D, -0.5D, 1.5D, 1.0D, 1.5D);

    public BlockDecorative() {
        super(Material.ROCK, Decor.class);

        setUnlocalizedName(Nomagi.MODID + ".decor");
        setCreativeTab(Nomagi.TAB_NOMAGI);
        setSoundType(SoundType.STONE);
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        super.getSubBlocks(itemIn, tab, list);
        list.remove(list.size() - 1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        if (state.getValue(getProperty()) != Decor.SOMETHING_SPECIAL)
            return super.getRenderType(state);
        String username = Minecraft.getMinecraft().player.getGameProfile().getName().toLowerCase(Locale.ENGLISH);
        if (!username.contains("eladkay") && !username.contains("tehnut"))
            return EnumBlockRenderType.INVISIBLE;

        return super.getRenderType(state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if (state.getValue(getProperty()) == Decor.SOMETHING_SPECIAL)
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1D, 0.1D, 0.1D);

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
        TENT_WALL_INSET(Material.CLOTH, SoundType.CLOTH),
        EBONSTONE_INSET(Material.ROCK, SoundType.STONE),
        SOMETHING_SPECIAL(Material.IRON, SoundType.METAL),
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
