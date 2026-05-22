package dev.sentix.squaremarker.marker;

import xyz.jpenilla.squaremap.api.WorldIdentifier;

public record Marker(
    int id,
    String content,
    String iconUrl,
    String iconKey,
    WorldIdentifier world,
    double posX,
    double posY,
    double posZ
) {}
