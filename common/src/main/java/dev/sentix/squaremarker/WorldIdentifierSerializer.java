package dev.sentix.squaremarker;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import java.lang.reflect.Type;

public final class WorldIdentifierSerializer implements JsonSerializer<WorldIdentifier>, JsonDeserializer<WorldIdentifier> {

    public static final WorldIdentifierSerializer INSTANCE = new WorldIdentifierSerializer();

    private WorldIdentifierSerializer() {}

    @Override
    public JsonElement serialize(WorldIdentifier src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) return JsonNull.INSTANCE;
        return new JsonPrimitive(src.asString());
    }

    @Override
    public WorldIdentifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json == null || json instanceof JsonNull) return null;
        return WorldIdentifier.parse(json.getAsString());
    }
}
