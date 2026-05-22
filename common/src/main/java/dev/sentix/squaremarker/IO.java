package dev.sentix.squaremarker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.sentix.squaremarker.marker.Marker;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public final class IO {

    private IO() {}

    public static Gson gson;
    private static Gson gsonPrettier;

    private static Path markerFile() {
        return SquareMarker.instance.markerFile;
    }

    public static void init() {
        gson = new GsonBuilder()
            .registerTypeAdapter(WorldIdentifier.class, SquareMarker.instance.worldIdentifierSerializer)
            .create();
        gsonPrettier = gson.newBuilder()
            .setPrettyPrinting()
            .create();

        if (!Files.exists(markerFile())) {
            write(Collections.emptyList());
        }
    }

    public static void write(List<Marker> input) {
        Path file = markerFile();
        try {
            if (!Files.exists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, gsonPrettier.toJson(input));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String read() {
        try {
            return Files.readString(markerFile());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
