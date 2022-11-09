package dev.klepto.dex.util;

import com.google.gson.*;
import dev.klepto.kweb3.type.Address;
import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

import static dev.klepto.kweb3.type.Address.address;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class JsonCache<T> {

    private final Class<T> type;
    private final Path cachePath;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Address.class, new AddressSerializer())
            .setPrettyPrinting().create();

    public JsonCache(Class<T> type, Path cachePath) {
        this.type = type;
        this.cachePath = cachePath;
    }

    @SneakyThrows
    public void serialize(T data) {
        val directory = cachePath.getParent();
        if (directory != null && !Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        try (val writer = Files.newBufferedWriter(cachePath, CREATE, TRUNCATE_EXISTING)) {
            gson.toJson(data, writer);
        }
    }

    @SneakyThrows
    public T deserialize() {
        if (!Files.exists(cachePath)) {
            return null;
        }

        try (val reader = Files.newBufferedReader(cachePath)) {
            return gson.fromJson(reader, type);
        }
    }

    public static class AddressSerializer implements JsonSerializer<Address>, JsonDeserializer<Address> {

        @Override
        public Address deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return address(json.getAsString());
        }

        @Override
        public JsonElement serialize(Address src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

}
