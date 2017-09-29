package info.sleeplessacorn.nomagi.world;

import info.sleeplessacorn.nomagi.ModObjects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderTent extends WorldProvider {

    private static final Vec3d GLOBAL_COLOR = new Vec3d(0.0D, 0.0D, 0.0D);
    private static final BiomeProvider BIOME = new BiomeProviderSingle(Biomes.VOID);
    private static final EmptyRenderer EMPTY_RENDERER = new EmptyRenderer();

    public WorldProviderTent() {}

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorTent(world);
    }

    @Override
    public Vec3d getFogColor(float f0, float f1) {
        return GLOBAL_COLOR;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return BIOME;
    }

    @Override
    public boolean hasSkyLight() {
        return true;
    }

    @Override
    public IRenderHandler getSkyRenderer() {
        return EMPTY_RENDERER;
    }

    @Override
    public IRenderHandler getCloudRenderer() {
        return EMPTY_RENDERER;
    }

    @Override
    public Vec3d getSkyColor(Entity entity, float partialTicks) {
        return GLOBAL_COLOR;
    }

    @Override
    public double getHorizon() {
        return 0;
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        return false;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return false;
    }

    @Override
    public DimensionType getDimensionType() {
        return ModObjects.TENT_DIMENSION;
    }

    private static class EmptyRenderer extends IRenderHandler {

        @Override
        public void render(float partialTicks, WorldClient world, Minecraft mc) {
            // no-op
        }

    }

}
