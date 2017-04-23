package info.sleeplessacorn.nomagi.world;

import info.sleeplessacorn.nomagi.core.ModObjects;
import net.minecraft.init.Biomes;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;

public class WorldProviderTent extends WorldProvider {

    @Override
    protected void init() {
        hasSkyLight = true;
        biomeProvider = new BiomeProviderSingle(Biomes.VOID);
    }

    @Override
    public DimensionType getDimensionType() {
        return ModObjects.TENT_DIMENSION;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderTent(world);
    }
}
