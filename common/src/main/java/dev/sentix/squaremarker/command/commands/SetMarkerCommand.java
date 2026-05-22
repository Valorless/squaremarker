package dev.sentix.squaremarker.command.commands;

import dev.sentix.squaremarker.Components;
import dev.sentix.squaremarker.SquareMarker;
import dev.sentix.squaremarker.command.Commands;
import dev.sentix.squaremarker.command.PlayerCommander;
import dev.sentix.squaremarker.command.SquaremarkerCommand;
import dev.sentix.squaremarker.marker.Marker;
import dev.sentix.squaremarker.marker.MarkerService;
import org.incendo.cloud.component.DefaultValue;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.minecraft.extras.RichDescription;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import javax.imageio.ImageIO;
import java.net.URI;
import java.util.concurrent.ThreadLocalRandom;

import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

public class SetMarkerCommand extends SquaremarkerCommand {

    public SetMarkerCommand(SquareMarker plugin, Commands commands) {
        super(plugin, commands);
    }

    @Override
    public void register() {
        commands.registerSubcommand(builder -> builder
            .literal("set")
            .optional("input", greedyStringParser(), DefaultValue.constant(" "))
            .commandDescription(RichDescription.richDescription(Components.parse("Set a marker at your position.")))
            .permission("squaremarker.set")
            .senderType(PlayerCommander.class)
            .handler(this::execute)
        );
    }

    private void execute(CommandContext<PlayerCommander> context) {
        PlayerCommander sender = context.sender();

        int id = ThreadLocalRandom.current().nextInt(9, 100000);

        String iconKey = "squaremarker_marker_icon_" + id;

        String input = context.get("input");

        String content = input;
        String url = "";

        if (input.contains("http")) {
            String[] split = input.split("http", 2);
            content = split[0];
            url = "http" + split[1];
        }

        Marker marker = new Marker(
            id,
            content.trim(),
            url.trim(),
            iconKey,
            sender.getWorld(),
            sender.getX(),
            sender.getY(),
            sender.getZ()
        );

        if (!MarkerService.markerExist(id)) {
            MarkerService.addMarker(marker);
            Components.sendPrefixed(sender, "<gray>Created marker with ID <color:#8411FB>" + id + "<gray>.</gray>");

            try {
                SquaremapProvider.get().iconRegistry().register(
                    Key.key(marker.iconKey()),
                    ImageIO.read(URI.create(marker.iconUrl()).toURL())
                );
            } catch (Exception ex) {
                Components.sendPrefixed(sender, "<gray>Marker icon set to default.");
            }
        }
    }
}
