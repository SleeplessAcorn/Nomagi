package info.sleeplessacorn.nomagi.core.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.data.Room.CustomizationJson.Matches;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import tehnut.lib.forge.json.serialization.SerializerBase;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SerializerCustomizationMatches extends SerializerBase<Matches> {

    @Override
    public Matches deserialize(
            JsonElement json, Type genericType, JsonDeserializationContext ctx) throws JsonParseException {
        Matches matches = new Matches();

        // Block matches
        Set<ResourceLocation> blockMatches = ctx.deserialize(json.getAsJsonObject().get("block"), new TypeToken<Set<ResourceLocation>>() {}.getType());
        matches.getBlockMatches().addAll(blockMatches);

        // State matches
        Set<IBlockState> stateMatches = ctx.deserialize(json.getAsJsonObject().getAsJsonArray("state"), new TypeToken<Set<IBlockState>>() {}.getType());
        matches.getStateMatches().addAll(stateMatches);

        // Instance matches
        Set<String> instanceMatches = ctx.deserialize(json.getAsJsonObject().getAsJsonArray("instance"), new TypeToken<Set<String>>() {}.getType());

        String className = "";
        try {
            for (String match : instanceMatches) {
                className = match;
                Class<?> type = Class.forName(className);
                if (!Block.class.isAssignableFrom(type)) {
                    String err = "Error adding instance check for {}. Class is not an instance of Block.";
                    Nomagi.LOGGER.error(err, className);
                    continue;
                }
                //noinspection unchecked - We checked above.
                matches.getInstanceMatches().add((Class<? extends Block>) type);
            }
        } catch (Exception e) {
            Nomagi.LOGGER.error("Error adding instance check for {}. Failed to load the class.", className);
        }

        return matches;
    }

    @Override
    public JsonElement serialize(Matches src, Type srcType, JsonSerializationContext ctx) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("block", ctx.serialize(src.getBlockMatches(), new TypeToken<Set<ResourceLocation>>() {}.getType()));
        jsonObject.add("state", ctx.serialize(src.getStateMatches(), new TypeToken<Set<IBlockState>>() {}.getType()));

        Set<String> instanceMatches = new HashSet<>();
        for (Class<? extends Block> aClass : src.getInstanceMatches()) {
            String canonicalName = aClass.getCanonicalName();
            instanceMatches.add(canonicalName);
        }

        jsonObject.add("instance", ctx.serialize(instanceMatches, new TypeToken<Set<String>>() {}.getType()));

        return jsonObject;
    }

    @Override
    public Class<?> getType() {
        return Matches.class;
    }
}
