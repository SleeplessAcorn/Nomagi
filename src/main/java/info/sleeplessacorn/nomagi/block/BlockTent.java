package info.sleeplessacorn.nomagi.block;

import com.google.common.collect.ImmutableMap;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.block.base.BlockEnumCardinalBase;
import info.sleeplessacorn.nomagi.block.base.IPropertyProvider;
import info.sleeplessacorn.nomagi.client.WrappedModel;
import info.sleeplessacorn.nomagi.client.WrappedModel.Builder;
import info.sleeplessacorn.nomagi.item.base.ItemBlockEnumBase;
import info.sleeplessacorn.nomagi.tile.TileTent;
import info.sleeplessacorn.nomagi.util.BoundingBoxHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

public class BlockTent extends BlockEnumCardinalBase<BlockTent.Variant> {

    public static final ImmutableMap<EnumFacing, AxisAlignedBB> AABB_TENT =
            BoundingBoxHelper.computeAABBsForFacing(-8, 0, 0, 24, 28, 32);

    public BlockTent() {
        super("tent", Variant.class);
        // TODO REWRITE THE SHIT OUT OF THIS
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(
            World world, IBlockState state) {
        return new TileTent();
    }

    @Override
    public ItemBlock getItemBlock() {
        return (ItemBlock) new ItemBlockEnumBase<Variant>(this) {
            @Override
            public void getModels(Set<WrappedModel> models) {
                ResourceLocation path = new ResourceLocation(Nomagi.ID, "tent_item");
                for (Variant value : values) {
                    models.add(new Builder(this, value.getMetadata()).setResourceLocation(path).addVariant("type=" + value.getName()).build());
                }
            }
        }.setMaxStackSize(1);
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB_TENT.get(getFacing(state));
    }

    public enum Variant implements IPropertyProvider<Variant> {

        BASIC; // TODO Handle variants in the tile entity. Maybe the variants could be dye colors?

        @Override
        public Variant getProvider() {
            return this;
        }

        @Override
        public Material getMaterial() {
            return Material.CLOTH;
        }

        @Override
        public boolean isFullCube() {
            return false;
        }

    }

}
