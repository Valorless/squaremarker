package dev.sentix.squaremarker.paper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import java.lang.reflect.Type;

public class PaperWorldIdentifierSerializer implements JsonSerializer<WorldIdentifier>, JsonDeserializer<WorldIdentifier> {

    private final Server server;

    public PaperWorldIdentifierSerializer(Server server) {
        this.server = server;
    }

    @Override
    public JsonElement serialize(WorldIdentifier src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) return JsonNull.INSTANCE;
        return new JsonPrimitive(src.asString());
    }

    @Override
    public WorldIdentifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json == null || json instanceof JsonNull) return null;
        NamespacedKey tryDeserializeKey = NamespacedKey.fromString(json.getAsString());
        if (tryDeserializeKey != null) {
            World world = server.getWorld(tryDeserializeKey);
            if (world != null) return WorldIdentifier.parse(json.getAsString());
        }
        World world = server.getWorld(json.getAsString());
        if (world == null) {
            try {
                return WorldIdentifier.parse(json.getAsString());
            } catch (Exception ex) {
                throw new JsonParseException(
                    "'" + json.getAsString() + "' is an invalid WorldIdentifier and no world exists with that name.",
                    ex
                );
            }
        }
        return BukkitAdapter.worldIdentifier(world);
    }
}
