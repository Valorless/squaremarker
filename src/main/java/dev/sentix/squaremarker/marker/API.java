package dev.sentix.squaremarker.marker;

import dev.sentix.squaremarker.Lang;
import dev.sentix.squaremarker.SquareMarker;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import javax.imageio.ImageIO;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class API {

    private API() {}

    public static final Key markerIconKey = Key.of("squaremarker_marker_icon_");

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final Map<WorldIdentifier, TaskEntry> providerMap = new HashMap<>();

    public static void init() {
        registerIcons();
        SquaremapProvider.get().mapWorlds().forEach(API::initWorld);
    }

    public static void unloadWorld(MapWorld mapWorld) {
        TaskEntry entry = providerMap.remove(mapWorld.identifier());
        if (entry != null) {
            entry.task().disable();
            entry.future().cancel(true);
        }
    }

    public static void initWorld(MapWorld mapWorld) {
        if (providerMap.containsKey(mapWorld.identifier())) {
            return;
        }

        SimpleLayerProvider provider = SimpleLayerProvider
            .builder(SquareMarker.instance.config.layerName)
            .defaultHidden(SquareMarker.instance.config.defaultHidden)
            .showControls(SquareMarker.instance.config.showControls)
            .build();

        Key key = Key.of("squaremarker_marker");
        if (mapWorld.layerRegistry().hasEntry(key)) {
            mapWorld.layerRegistry().unregister(key);
        }
        mapWorld.layerRegistry().register(key, provider);

        MarkerTask task = new MarkerTask(mapWorld, provider);
        ScheduledFuture<?> scheduled = executor.scheduleAtFixedRate(
            task,
            0,
            SquareMarker.instance.config.updateRateMilliseconds,
            TimeUnit.MILLISECONDS
        );
        providerMap.put(mapWorld.identifier(), new TaskEntry(scheduled, task));
    }

    private static void registerIcons() {
        for (dev.sentix.squaremarker.marker.Marker marker : MarkerService.getMarkerList()) {
            if (!marker.iconUrl().isBlank()) {
                try {
                    SquaremapProvider.get().iconRegistry().register(
                        Key.of(marker.iconKey()),
                        ImageIO.read(URI.create(marker.iconUrl()).toURL())
                    );
                } catch (Exception ex) {
                    SquareMarker.logger.warn(
                        "{} There is an invalid url in your marker.json. Please fix \"{}\"!",
                        Lang.PLAIN_PREFIX, marker.iconUrl(), ex
                    );
                }
            }
        }
    }

    public static void cancel(WorldIdentifier world) {
        TaskEntry entry = providerMap.get(world);
        if (entry != null) {
            entry.future().cancel(true);
        }
    }

    public static void unregister() {
        for (TaskEntry entry : providerMap.values()) {
            entry.task().disable();
            entry.future().cancel(true);
        }
        providerMap.clear();
        executor.shutdownNow();
    }

    private record TaskEntry(ScheduledFuture<?> future, MarkerTask task) {}
}
