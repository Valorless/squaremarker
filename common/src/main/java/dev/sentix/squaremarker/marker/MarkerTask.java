package dev.sentix.squaremarker.marker;

import dev.sentix.squaremarker.SquareMarker;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

public class MarkerTask implements Runnable {

    private final MapWorld world;
    private final SimpleLayerProvider provider;
    private volatile boolean stop = false;

    public MarkerTask(MapWorld world, SimpleLayerProvider provider) {
        this.world = world;
        this.provider = provider;
    }

    @Override
    public void run() {
        if (stop) {
            API.cancel(world.identifier());
            return;
        }

        provider.clearMarkers();
        for (dev.sentix.squaremarker.marker.Marker marker : MarkerService.getMarkerList()) {
            if (marker.world().equals(world.identifier())) {
                Key iconKey = !marker.iconUrl().isEmpty()
                    ? Key.of(marker.iconKey())
                    : API.markerIconKey;
                handle(marker.id(), marker.content(), iconKey, marker.posX(), marker.posZ());
            }
        }
    }

    private void handle(int id, String name, Key iconKey, double x, double z) {
        Icon icon = Marker.icon(Point.point(x, z), iconKey, SquareMarker.instance.config.iconSize);
        if (!name.isBlank()) {
            icon.markerOptions(
                MarkerOptions.builder().hoverTooltip("<center>" + name + "</center>")
            );
        }
        String markerId = "squaremarker_marker_" + id;
        provider.addMarker(Key.of(markerId), icon);
    }

    public void disable() {
        stop = true;
        provider.clearMarkers();
    }
}
