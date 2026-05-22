package dev.sentix.squaremarker.paper;

import dev.sentix.squaremarker.SquareMarker;
import dev.sentix.squaremarker.command.Commander;
import dev.sentix.squaremarker.marker.API;
import dev.sentix.squaremarker.paper.command.PaperCommander;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public class SquareMarkerPlugin extends JavaPlugin implements Listener {

    private SquareMarker squareMarker;

    @Override
    public void onEnable() {
        squareMarker = new SquareMarker(
            createCommandManager(),
            getDataFolder().toPath().resolve("config.yml"),
            getDataFolder().toPath(),
            new PaperWorldIdentifierSerializer(getServer())
        );
        squareMarker.init();

        getServer().getPluginManager().registerEvents(this, this);

        // https://bstats.org/plugin/bukkit/squaremarker/14117
        new Metrics(this, 14117);
    }

    @Override
    public void onDisable() {
        squareMarker.shutdown();
    }

    private LegacyPaperCommandManager<Commander> createCommandManager() {
        LegacyPaperCommandManager<Commander> mgr = new LegacyPaperCommandManager<>(
            this,
            ExecutionCoordinator.<Commander>builder()
                .synchronizeExecution(Folia.FOLIA)
                .build(),
            SenderMapper.create(
                sender -> PaperCommander.create(sender),
                commander -> ((PaperCommander) commander).getSender()
            )
        );
        mgr.registerBrigadier();
        return mgr;
    }

    @EventHandler
    public void handleLoad(WorldLoadEvent event) {
        SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(event.getWorld())).ifPresent(API::initWorld);
    }

    @EventHandler
    public void handleUnLoad(WorldLoadEvent event) {
        SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(event.getWorld())).ifPresent(API::unloadWorld);
    }
}
