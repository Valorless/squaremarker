package dev.sentix.squaremarker;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class Configuration {
    public String commandLabel = "squaremarker";
    public List<String> commandAliases = new ArrayList<>(List.of("marker", "squaremapmarker", "smarker"));
    public String layerName = "Marker";
    public String iconUrl = "https://github.com/SentixDev/squaremarker/raw/master/resources/default_icon.png";
    public int iconSize = 16;
    public boolean showControls = true;
    public boolean defaultHidden = false;
    public long updateRateMilliseconds = 5L * 1000L; // 5 seconds
}
