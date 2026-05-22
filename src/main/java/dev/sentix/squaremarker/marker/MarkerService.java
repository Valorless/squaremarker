package dev.sentix.squaremarker.marker;

import com.google.gson.reflect.TypeToken;
import dev.sentix.squaremarker.IO;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public final class MarkerService {

    private MarkerService() {}

    public static List<Marker> getMarkerList() {
        Type type = new TypeToken<List<Marker>>() {}.getType();
        return IO.gson.fromJson(IO.read(), type);
    }

    public static Marker getMarker(int id) {
        return getMarkerList().stream()
            .filter(m -> m.id() == id)
            .findFirst()
            .orElseThrow(() -> new IndexOutOfBoundsException("No marker with id " + id));
    }

    public static boolean markerExist(int id) {
        try {
            getMarker(id);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static void addMarker(Marker marker) {
        List<Marker> markerList = getMarkerList();
        markerList.add(marker);
        IO.write(markerList);
    }

    public static void removeMarker(int id) {
        List<Marker> filtered = getMarkerList().stream()
            .filter(m -> m.id() != id)
            .collect(Collectors.toList());
        IO.write(filtered);
    }

    public static void updateMarker(Marker marker) {
        removeMarker(marker.id());
        addMarker(marker);
    }
}
