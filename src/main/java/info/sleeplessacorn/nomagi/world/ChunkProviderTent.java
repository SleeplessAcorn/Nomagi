package info.sleeplessacorn.nomagi.world;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkProviderFlat;

public class ChunkProviderTent extends ChunkProviderFlat {

    private World world;

    public ChunkProviderTent(World world) {
        super(world, world.getSeed(), false, null);

        this.world = world;
        this.world.setSeaLevel(0);
    }

    @Override
    public void populate(int x, int z) {
        // No-op
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        Chunk ret = new Chunk(world, new ChunkPrimer(), x, z);
        Biome[] biomes = world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        byte[] ids = ret.getBiomeArray();

        for (int i = 0; i < ids.length; ++i)
            ids[i] = (byte) Biome.getIdForBiome(biomes[i]);

        ret.generateSkylightMap();
        return ret;
    }
}
