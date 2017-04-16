package info.sleeplessacorn.nomagi.core.json;

import com.google.common.collect.Sets;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import info.sleeplessacorn.nomagi.Nomagi;
import info.sleeplessacorn.nomagi.core.data.Room;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import tehnut.lib.forge.json.serialization.SerializerBase;

import java.lang.reflect.Type;
import java.util.Set;

public class SerializerCustomizationMatches extends SerializerBase<Room.CustomizationJson.Matches> {

    @Override
    public Room.CustomizationJson.Matches deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Room.CustomizationJson.Matches matches = new Room.CustomizationJson.Matches();

        // Block matches
        Set<ResourceLocation> blockMatches = context.deserialize(json.getAsJsonObject().get("block"), new TypeToken<Set<ResourceLocation>>(){}.getType());
        matches.getBlockMatches().addAll(blockMatches);

        // State matches
        Set<IBlockState> stateMatches = context.deserialize(json.getAsJsonObject().getAsJsonArray("state"), new TypeToken<Set<IBlockState>>(){}.getType());
        matches.getStateMatches().addAll(stateMatches);

        // Instance matches
        Set<String> instanceMatches = context.deserialize(json.getAsJsonObject().getAsJsonArray("instance"), new TypeToken<Set<String>>(){}.getType());
        String className = "";
        try {
            for (String instance : instanceMatches) {
                className = instance;
                Class<?> type = Class.forName(className);
                if (!Block.class.isAssignableFrom(type)) {
                    Nomagi.LOGGER.error("Error adding instance check for {}. Class does not inherit from Block.", className);
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
    public JsonElement serialize(Room.CustomizationJson.Matches src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("block", context.serialize(src.getBlockMatches(), new TypeToken<Set<ResourceLocation>>(){}.getType()));
        jsonObject.add("state", context.serialize(src.getStateMatches(), new TypeToken<Set<IBlockState>>(){}.getType()));

        Set<String> instanceMatches = Sets.newHashSet();
        for (Class<? extends Block> blockClass : src.getInstanceMatches())
            instanceMatches.add(blockClass.getCanonicalName());
        jsonObject.add("instance", context.serialize(instanceMatches, new TypeToken<Set<String>>(){}.getType()));

        return jsonObject;
    }

    @Override
    public Class<?> getType() {
        return Room.CustomizationJson.Matches.class;
    }
}
