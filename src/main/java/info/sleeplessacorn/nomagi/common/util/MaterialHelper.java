package info.sleeplessacorn.nomagi.common.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public final class MaterialHelper {

    private static final MaterialData DEFAULT = new MaterialData(SoundType.STONE, 00.00f, 00.00f);

    private static final ImmutableMap<Material, MaterialData> MATERIAL_DATA = ImmutableMap.<Material, MaterialData>
            builder()
            .put(Material.GROUND, new MaterialData(SoundType.GROUND, 00.50f, 02.50f))
            .put(Material.WOOD, new MaterialData(SoundType.WOOD, 02.00f, 15.00f))
            .put(Material.ROCK, new MaterialData(SoundType.STONE, 01.50f, 30.00f))
            .put(Material.IRON, new MaterialData(SoundType.METAL, 05.00f, 30.00f))
            .put(Material.LEAVES, new MaterialData(SoundType.PLANT, 00.20f, 01.00f))
            .put(Material.PLANTS, new MaterialData(SoundType.PLANT, 00.00f, 00.00f))
            .put(Material.VINE, new MaterialData(SoundType.PLANT, 00.20f, 01.00f))
            .put(Material.CLOTH, new MaterialData(SoundType.CLOTH, 00.80f, 04.00f))
            .put(Material.SAND, new MaterialData(SoundType.SAND, 00.50f, 02.50f))
            .put(Material.CIRCUITS, new MaterialData(SoundType.STONE, 00.00f, 00.00f))
            .put(Material.CARPET, new MaterialData(SoundType.CLOTH, 00.10f, 00.50f))
            .put(Material.GLASS, new MaterialData(SoundType.GLASS, 00.30f, 01.50f))
            .put(Material.ICE, new MaterialData(SoundType.GLASS, 00.50f, 02.50f))
            .build();

    public static float getHardness(Material material) {
        return MATERIAL_DATA.getOrDefault(material, DEFAULT).hardness;
    }

    public static float getResistance(Material material) {
        return MATERIAL_DATA.getOrDefault(material, DEFAULT).resistance;
    }

    public static SoundType getSoundType(Material material) {
        return MATERIAL_DATA.getOrDefault(material, DEFAULT).sound;
    }

    private static final class MaterialData {

        private final SoundType sound;
        private final float hardness;
        private final float resistance;

        private MaterialData(SoundType sound, float hardness, float resistance) {
            this.sound = sound;
            this.hardness = hardness;
            this.resistance = resistance;
        }

    }

}