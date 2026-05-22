package dev.sentix.squaremarker.command.commands;

import dev.sentix.squaremarker.Components;
import dev.sentix.squaremarker.Lang;
import dev.sentix.squaremarker.SquareMarker;
import dev.sentix.squaremarker.command.Commander;
import dev.sentix.squaremarker.command.Commands;
import dev.sentix.squaremarker.command.SquaremarkerCommand;
import dev.sentix.squaremarker.marker.Marker;
import dev.sentix.squaremarker.marker.MarkerService;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.minecraft.extras.RichDescription;

import java.util.List;

public class ListMarkerCommand extends SquaremarkerCommand {

    public ListMarkerCommand(SquareMarker plugin, Commands commands) {
        super(plugin, commands);
    }

    @Override
    public void register() {
        commands.registerSubcommand(builder -> builder
            .literal("list")
            .commandDescription(RichDescription.richDescription(Components.parse("List all markers.")))
            .permission("squaremarker.list")
            .handler(this::execute)
        );
    }

    private void execute(CommandContext<Commander> context) {
        Commander sender = context.sender();

        List<Marker> markerList = MarkerService.getMarkerList();

        if (!markerList.isEmpty()) {
            sendMarkerList(sender, markerList);
        } else {
            Components.send(sender, Lang.EMPTY);
        }
    }

    private void sendMarkerList(Commander sender, List<Marker> markerList) {
        String gradient = Components.gradient("<b>Marker</b>");

        Components.send(sender, "");
        Components.send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<reset> <gray>× " + gradient + " <gray>× <dark_gray><st>-------------<reset> <dark_gray>«"
        );
        Components.send(sender, "");

        Components.send(sender, " <gray>× <color:#8411FB>Markers <gray>[<color:#8411FB>" + markerList.size() + "<gray>]");
        Components.send(sender, "");
        for (Marker marker : markerList) {
            Components.send(
                sender,
                " <gray>× <color:#8411FB>" + marker.id() + " <color:#8411FB>" +
                    Components.clickable(
                        "<dark_gray>[<color:#8411FB>SHOW</color>]",
                        "<color:#8411FB>SHOW",
                        "/squaremarker show " + marker.id()
                    )
            );
        }

        Components.send(sender, "");
        Components.send(
            sender,
            "<dark_gray>» <dark_gray><st>-------------<reset> <gray>× " + gradient + " <gray>× <dark_gray><st>-------------<reset> <dark_gray>«"
        );
        Components.send(sender, "");
    }
}
