package info.sleeplessacorn.nomagi.core.json;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import info.sleeplessacorn.nomagi.core.data.Room;
import net.minecraft.util.math.BlockPos;
import tehnut.lib.forge.json.serialization.SerializerBase;

import java.lang.reflect.Type;
import java.util.Map;

public class SerializerCustomization extends SerializerBase<Room.CustomizationJson> {

    @Override
    public Room.CustomizationJson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, Room.CustomizationJson.Matches> stringMatches = context.deserialize(json.getAsJsonObject().get("matches"), new TypeToken<Map<String, Room.CustomizationJson.Matches>>() {}.getType());
        Map<BlockPos, Room.CustomizationJson.Matches> matches = Maps.newHashMap();
        for (Map.Entry<String, Room.CustomizationJson.Matches> match : stringMatches.entrySet())
            matches.put(stringToPos(match.getKey()), match.getValue());

        return new Room.CustomizationJson(matches);
    }

    @Override
    public JsonElement serialize(Room.CustomizationJson src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        Map<String, Room.CustomizationJson.Matches> matches = Maps.newHashMap();
        for (Map.Entry<BlockPos, Room.CustomizationJson.Matches> match : src.getMatches().entrySet())
            matches.put(posToString(match.getKey()), match.getValue());

        jsonObject.add("matches", context.serialize(matches, new TypeToken<Map<String, Room.CustomizationJson.Matches>>() {}.getType()));

        return jsonObject;
    }

    @Override
    public Class<?> getType() {
        return null;
    }

    private static String posToString(BlockPos pos) {
        return String.format("%d,%d,%d", pos.getX(), pos.getY(), pos.getZ());
    }

    private static BlockPos stringToPos(String value) {
        String[] split = value.split(",");
        return new BlockPos(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }
}
