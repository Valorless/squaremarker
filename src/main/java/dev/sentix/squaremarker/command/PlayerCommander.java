package dev.sentix.squaremarker.command;

import xyz.jpenilla.squaremap.api.WorldIdentifier;

public interface PlayerCommander extends Commander {
    WorldIdentifier getWorld();
    double getX();
    double getY();
    double getZ();
}
