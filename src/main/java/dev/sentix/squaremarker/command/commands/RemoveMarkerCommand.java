package dev.sentix.squaremarker.command.commands;

import dev.sentix.squaremarker.Components;
import dev.sentix.squaremarker.SquareMarker;
import dev.sentix.squaremarker.command.Commander;
import dev.sentix.squaremarker.command.Commands;
import dev.sentix.squaremarker.command.SquaremarkerCommand;
import dev.sentix.squaremarker.marker.MarkerService;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.minecraft.extras.RichDescription;

import static org.incendo.cloud.parser.standard.IntegerParser.integerParser;

public class RemoveMarkerCommand extends SquaremarkerCommand {

    public RemoveMarkerCommand(SquareMarker plugin, Commands commands) {
        super(plugin, commands);
    }

    @Override
    public void register() {
        commands.registerSubcommand(builder -> builder
            .literal("remove")
            .required("id", integerParser())
            .commandDescription(RichDescription.richDescription(Components.parse("Remove a marker by id.")))
            .permission("squaremarker.remove")
            .handler(this::execute)
        );
    }

    private void execute(CommandContext<Commander> context) {
        Commander sender = context.sender();

        int id = context.get("id");

        if (MarkerService.markerExist(id)) {
            MarkerService.removeMarker(id);
            Components.sendPrefixed(sender, "<gray>Removed marker with ID <color:#8411FB>" + id + "<gray>.</gray>");
        } else {
            Components.sendPrefixed(sender, "<gray>No marker with ID <color:#8411FB>" + id + " <gray>found.</gray>");
        }
    }
}
