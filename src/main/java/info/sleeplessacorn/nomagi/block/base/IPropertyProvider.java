package info.sleeplessacorn.nomagi.block.base;

import info.sleeplessacorn.nomagi.util.MaterialHelper;
import info.sleeplessacorn.nomagi.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Locale;

public interface IPropertyProvider <E extends Enum<E> & IStringSerializable> extends IStringSerializable {

    E getProvider();

    Material getMaterial();

    default float getHardness() {
        return MaterialHelper.getHardness(this.getMaterial());
    }

    default float getResistance() {
        return MaterialHelper.getResistance(this.getMaterial());
    }

    default SoundType getSoundType() {
        return MaterialHelper.getSoundType(this.getMaterial());
    }

    default BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.SOLID;
    }

    default String getOrePrefix() {
        return "block";
    }

    default int getLightLevel() {
        return 0;
    }

    default int getLightOpacity() {
        return 255;
    }

    default String getEffectiveTool() {
        return null;
    }

    default AxisAlignedBB getBoundingBox() {
        return Block.FULL_BLOCK_AABB;
    }

    default String getName() {
        return this.getProvider().name().toLowerCase(Locale.ROOT);
    }

    default String getUnlocalizedName() {
        return StringHelper.toLangKey(this.getName());
    }

    default int getMetadata() {
        return this.getProvider().ordinal();
    }

    default String getOreDict() {
        // TODO Automate ore dictionary registration for instances of BlockEnumBase
        return this.getOrePrefix() + StringHelper.getOreName(this.getName());
    }

    default boolean requiresSilkTouch() {
        return false;
    }

    default boolean isFullCube() {
        return true;
    }

    default boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

}

