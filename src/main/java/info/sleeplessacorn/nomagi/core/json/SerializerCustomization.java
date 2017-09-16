package info.sleeplessacorn.nomagi.core.json;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import info.sleeplessacorn.nomagi.core.data.Room;
import info.sleeplessacorn.nomagi.core.data.Room.CustomizationJson.Matches;
import net.minecraft.util.math.BlockPos;
import tehnut.lib.forge.json.serialization.SerializerBase;

import java.lang.reflect.Type;
import java.util.Map;

public class SerializerCustomization extends SerializerBase<Room.CustomizationJson> {

    @Override
    public Room.CustomizationJson deserialize(
            JsonElement json, Type genericType, JsonDeserializationContext ctx) throws JsonParseException {

        Map<String, Matches> stringMatches = ctx.deserialize(json.getAsJsonObject().get("matches"),
                new TypeToken<Map<String, Matches>>() {}.getType());

        Map<BlockPos, Matches> matches = Maps.newHashMap();

        stringMatches.forEach((key, value) -> matches.put(stringToPos(key), value));

        return new Room.CustomizationJson(matches);
    }

    @Override
    public JsonElement serialize(Room.CustomizationJson src, Type srcType, JsonSerializationContext ctx) {
        JsonObject jsonObject = new JsonObject();
        Map<String, Matches> matches = Maps.newHashMap();

        src.getMatches().forEach((key, value) -> matches.put(posToString(key), value));

        jsonObject.add("matches", ctx.serialize(matches, new TypeToken<Map<String, Matches>>() {}.getType()));

        return jsonObject;
    }

    @Override
    public Class<?> getType() {
        return null;
    }

    private static String posToString(BlockPos pos) {
        return pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    private static BlockPos stringToPos(String posString) {
        String[] split = posString.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        int z = Integer.parseInt(split[2]);
        return new BlockPos(x, y, z);
    }

}
