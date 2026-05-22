package dev.sentix.squaremarker.paper.command;

import dev.sentix.squaremarker.command.Commander;
import dev.sentix.squaremarker.command.PlayerCommander;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.command.CommandSender;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public class PaperCommander implements Commander, ForwardingAudience.Single {

    private final CommandSender sender;

    public PaperCommander(CommandSender sender) {
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }

    @Override
    public Audience audience() {
        return sender;
    }

    public static Commander create(CommandSender sender) {
        if (sender instanceof org.bukkit.entity.Player player) {
            return new Player(player);
        }
        return new PaperCommander(sender);
    }

    public static class Player extends PaperCommander implements PlayerCommander {

        private final org.bukkit.entity.Player player;

        public Player(org.bukkit.entity.Player player) {
            super(player);
            this.player = player;
        }

        @Override
        public WorldIdentifier getWorld() {
            return BukkitAdapter.worldIdentifier(player.getWorld());
        }

        @Override
        public double getX() {
            return player.getLocation().getX();
        }

        @Override
        public double getY() {
            return player.getLocation().getY();
        }

        @Override
        public double getZ() {
            return player.getLocation().getZ();
        }
    }
}
