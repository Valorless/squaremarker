package dev.sentix.squaremarker;

public final class Lang {

    private Lang() {}

    public static final String PLAIN_PREFIX = "[squaremarker]";
    public static final String PREFIX = "<gray>[<gradient:#C028FF:#5B00FF>squaremarker</gradient>]</gray>";

    public static final String NO_PERMISSION = "<red>Not authorized.</red>";

    public static final String EMPTY = PREFIX + " <red>No markers set.</red>";

    public static String getHelp() {
        return Components.clickable(
            PREFIX + " ",
            Components.gradient("Click for SquareMarker command help"),
            "/" + SquareMarker.instance.config.commandLabel + " help"
        );
    }
}
