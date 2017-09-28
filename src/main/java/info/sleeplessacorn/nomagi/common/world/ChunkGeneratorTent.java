package info.sleeplessacorn.nomagi.common.world;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorFlat;

public class ChunkGeneratorTent extends ChunkGeneratorFlat {

    private World world;

    public ChunkGeneratorTent(World world) {
        super(world, world.getSeed(), false, null);
        this.world = world;
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        Chunk chunk = new Chunk(world, new ChunkPrimer(), x, z);
        Biome[] biomes = world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        byte[] ids = chunk.getBiomeArray();
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = (byte) Biome.getIdForBiome(biomes[i]);
        }
        chunk.checkLight();
        return chunk;
    }

    @Override
    public void populate(int x, int z) {
        // No-op
    }

}