package dev.sentix.squaremarker;

import dev.sentix.squaremarker.command.Commander;
import dev.sentix.squaremarker.command.Commands;
import dev.sentix.squaremarker.marker.API;
import org.incendo.cloud.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public class SquareMarker {

    public static final Logger logger = LoggerFactory.getLogger(SquareMarker.class);
    public static SquareMarker instance;

    public final Path markerFile;
    public final Configuration config;
    public final Object worldIdentifierSerializer;

    public SquareMarker(
        CommandManager<Commander> commandManager,
        Path configFile,
        Path dataDir,
        Object worldIdentifierSerializer
    ) {
        this.worldIdentifierSerializer = worldIdentifierSerializer;
        this.markerFile = dataDir.resolve("marker.json");
        this.config = loadConfiguration(configFile);

        instance = this;

        new Commands(this, commandManager).registerCommands();
    }

    public void init() {
        IO.init();
        API.init();
    }

    public void shutdown() {
        API.unregister();
    }

    private Configuration loadConfiguration(Path configFile) {
        try {
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
            ConfigurationNode node = loader.load();
            Configuration configInst = node.get(Configuration.class);
            if (configInst == null) {
                throw new IllegalStateException("Error reading config");
            }
            ConfigurationNode save = loader.createNode();
            save.set(configInst);
            loader.save(save);
            return configInst;
        } catch (ConfigurateException e) {
            throw new RuntimeException("Error loading configuration", e);
        }
    }
}
