package dev.sentix.squaremarker.paper;

public final class Folia {

    private Folia() {}

    public static final boolean FOLIA = computeFolia();

    private static boolean computeFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
