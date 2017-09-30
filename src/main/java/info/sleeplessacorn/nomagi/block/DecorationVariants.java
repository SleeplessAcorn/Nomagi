package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.block.base.IPropertyProvider;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public enum DecorationVariants implements IPropertyProvider<DecorationVariants> {

    BRICKS(Material.ROCK),
    BRICKS_INSET(Material.ROCK),
    FABRIC(Material.CLOTH),
    FABRIC_INSET(Material.CLOTH),
    FABRIC_RIBBON(Material.CLOTH),
    GLASS(Material.GLASS) {
        @Override
        public int getLightOpacity() {
            return 0;
        }

        @Override
        public BlockRenderLayer getRenderLayer() {
            return BlockRenderLayer.TRANSLUCENT;
        }

        @Override
        public boolean isFullCube() {
            return false;
        }
    },
    PLANKS(Material.WOOD),
    STONE(Material.ROCK);

    private final Material material;

    DecorationVariants(Material material) {
        this.material = material;
    }

    @Override
    public DecorationVariants getProvider() {
        return this;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

}
