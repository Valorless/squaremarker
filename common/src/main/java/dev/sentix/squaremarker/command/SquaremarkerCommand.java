package dev.sentix.squaremarker.command;

import dev.sentix.squaremarker.SquareMarker;

public abstract class SquaremarkerCommand {

    protected final SquareMarker squareMarker;
    protected final Commands commands;

    protected SquaremarkerCommand(SquareMarker squareMarker, Commands commands) {
        this.squareMarker = squareMarker;
        this.commands = commands;
    }

    public abstract void register();
}
