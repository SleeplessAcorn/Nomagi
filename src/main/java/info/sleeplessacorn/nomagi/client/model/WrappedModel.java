package info.sleeplessacorn.nomagi.client.model;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.TreeSet;

public final class WrappedModel {

    private final Item item;
    private final int meta;
    private final ResourceLocation resource;
    private final String variants;
    private final ModelResourceLocation mrl;

    private WrappedModel(Builder model) {
        this.item = model.item;
        this.meta = model.meta;
        this.resource = model.resource;
        this.variants = String.join(",", model.variants);
        this.mrl = new ModelResourceLocation(this.resource, this.variants);
    }

    public Item getItem() {
        return item;
    }

    public int getMetadata() {
        return meta;
    }

    public ResourceLocation getResourceLocation() {
        return resource;
    }

    public String getVariants() {
        return variants;
    }

    public ModelResourceLocation getMRL() {
        return mrl;
    }

    public static final class Builder {

        private Item item;
        private int meta;
        private ResourceLocation resource;
        private TreeSet<String> variants = new TreeSet<>();

        public Builder(Item item, int meta) {
            this.item = item;
            this.meta = meta;
            this.resource = item.getRegistryName();
        }

        public Builder(Item item) {
            this(item, 0);
        }

        public Builder setResourceLocation(ResourceLocation resource) {
            this.resource = resource;
            return this;
        }

        public Builder addVariant(String variant) {
            this.variants.add(variant);
            return this;
        }

        public WrappedModel build() {
            if (variants.isEmpty()) {
                variants.add("inventory");
            }
            return new WrappedModel(this);
        }

    }

}
