package info.sleeplessacorn.nomagi.core.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.json.SerializerBlockState;
import info.sleeplessacorn.nomagi.core.json.SerializerCustomization;
import info.sleeplessacorn.nomagi.core.json.SerializerCustomizationMatches;
import info.sleeplessacorn.nomagi.core.json.SerializerResourceLocation;
import info.sleeplessacorn.nomagi.util.GeneratorUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

public class Room {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .serializeNulls()
            .registerTypeHierarchyAdapter(IBlockState.class, new SerializerBlockState())
            .registerTypeAdapter(CustomizationJson.class, new SerializerCustomization())
            .registerTypeAdapter(CustomizationJson.Matches.class, new SerializerCustomizationMatches())
            .registerTypeAdapter(ResourceLocation.class, new SerializerResourceLocation())
            .create();

    private transient final String name;
    private transient Customization customization;
    private final ResourceLocation schematic;

    public Room(ResourceLocation schematic) {
        this.name = "tent." + schematic.toString() + ".name";
        this.customization = CustomizationJson.fromFile(schematic);
        this.schematic = schematic;
    }

    public String getName() {
        return name;
    }

    public Customization getCustomization() {
        return customization;
    }

    public ResourceLocation getSchematic() {
        return schematic;
    }

    @Nullable
    public Template getTemplate() {
        return GeneratorUtil.readFromId(schematic);
    }

    public static class Customization {

        public boolean canPlaceBlock(World world, BlockPos roomOffset, IBlockState state) {
            return true;
        }
    }

    public static class CustomizationJson extends Customization {

        private final Map<BlockPos, Matches> matches;

        public CustomizationJson(Map<BlockPos, Matches> matches) {
            this.matches = matches;
        }

        @Override
        public boolean canPlaceBlock(World world, BlockPos roomOffset, IBlockState state) {
            Matches posMatches = matches.get(roomOffset);
            if (posMatches == null)
                return false;

            if (posMatches.getStateMatches().contains(state))
                return true;

            if (posMatches.getBlockMatches().contains(state.getBlock().getRegistryName()))
                return true;

            for (Class<? extends Block> blockClass : posMatches.getInstanceMatches())
                if (blockClass.isAssignableFrom(state.getBlock().getClass()))
                    return true;

            return false;
        }

        public Map<BlockPos, Matches> getMatches() {
            return matches;
        }

        public static Customization fromFile(ResourceLocation id) {
            InputStream stream = Nomagi.class.getResourceAsStream("/assets/" + id.getResourceDomain() + "/rooms/" + id.getResourcePath() + ".json");
            if (stream == null)
                return new Customization();

            return GSON.fromJson(new InputStreamReader(stream), CustomizationJson.class);
        }

        public static class Matches {
            private final Set<IBlockState> stateMatches;
            private final Set<ResourceLocation> blockMatches;
            private final Set<Class<? extends Block>> instanceMatches;

            public Matches(Set<IBlockState> stateMatches, Set<ResourceLocation> blockMatches, Set<Class<? extends Block>> instanceMatches) {
                this.stateMatches = stateMatches;
                this.blockMatches = blockMatches;
                this.instanceMatches = instanceMatches;
            }

            public Matches() {
                this.stateMatches = Sets.newHashSet();
                this.blockMatches = Sets.newHashSet();
                this.instanceMatches = Sets.newHashSet();
            }

            public Set<IBlockState> getStateMatches() {
                return stateMatches;
            }

            public Set<ResourceLocation> getBlockMatches() {
                return blockMatches;
            }

            public Set<Class<? extends Block>> getInstanceMatches() {
                return instanceMatches;
            }
        }
    }
}
