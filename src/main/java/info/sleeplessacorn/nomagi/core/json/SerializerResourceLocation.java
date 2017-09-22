package info.sleeplessacorn.nomagi.core.json;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import tehnut.lib.forge.json.serialization.SerializerBase;

import java.lang.reflect.Type;

public class SerializerResourceLocation extends SerializerBase<ResourceLocation> {

    @Override
    public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ResourceLocation(json.getAsString());
    }

    @Override
    public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Class<?> getType() {
        return ResourceLocation.class;
    }
}
