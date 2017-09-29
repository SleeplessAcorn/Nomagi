package info.sleeplessacorn.nomagi.block;

import info.sleeplessacorn.nomagi.block.base.BlockEnumBase;
import info.sleeplessacorn.nomagi.block.base.IPropertyProvider;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

public class BlockDecorative extends BlockEnumBase<BlockDecorative.Variant> {

    public BlockDecorative() {
        super("decorative", Variant.class);
    }

    public enum Variant implements IPropertyProvider<Variant> {

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

        Variant(Material material) {
            this.material = material;
        }

        @Override
        public Variant getProvider() {
            return this;
        }

        @Override
        public Material getMaterial() {
            return material;
        }

    }

}
