package dev.sentix.squaremarker.command;

import dev.sentix.squaremarker.Components;
import dev.sentix.squaremarker.Lang;
import dev.sentix.squaremarker.SquareMarker;
import dev.sentix.squaremarker.command.commands.HelpCommand;
import dev.sentix.squaremarker.command.commands.ListMarkerCommand;
import dev.sentix.squaremarker.command.commands.RemoveMarkerCommand;
import dev.sentix.squaremarker.command.commands.SetMarkerCommand;
import dev.sentix.squaremarker.command.commands.ShowMarkerCommand;
import dev.sentix.squaremarker.command.commands.UpdateMarkerCommand;
import net.kyori.adventure.text.format.NamedTextColor;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.description.Description;
import org.incendo.cloud.exception.InvalidCommandSenderException;
import org.incendo.cloud.exception.NoPermissionException;
import org.incendo.cloud.minecraft.extras.MinecraftExceptionHandler;
import org.incendo.cloud.util.TypeUtils;

import java.util.List;
import java.util.function.Function;

import static net.kyori.adventure.text.Component.text;

public class Commands {

    private final SquareMarker squareMarker;
    public final CommandManager<Commander> commandManager;

    public Commands(SquareMarker squareMarker, CommandManager<Commander> commandManager) {
        this.squareMarker = squareMarker;
        this.commandManager = commandManager;
        registerExceptionHandlers();
    }

    public void registerCommands() {
        List.of(
            new HelpCommand(squareMarker, this),
            new ListMarkerCommand(squareMarker, this),
            new RemoveMarkerCommand(squareMarker, this),
            new SetMarkerCommand(squareMarker, this),
            new ShowMarkerCommand(squareMarker, this),
            new UpdateMarkerCommand(squareMarker, this)
        ).forEach(SquaremarkerCommand::register);
    }

    private void registerExceptionHandlers() {
        MinecraftExceptionHandler.<Commander>createNative()
            .defaultArgumentParsingHandler()
            .defaultInvalidSenderHandler()
            .defaultCommandExecutionHandler()
            .handler(InvalidCommandSenderException.class, (formatter, ctx) -> {
                java.lang.reflect.Type requiredType = ctx.exception().requiredSenderTypes().iterator().next();
                String requiredTypeDisplayName = requiredType == PlayerCommander.class
                    ? "Players"
                    : TypeUtils.simpleName(requiredType);
                return text()
                    .content("This command can only be executed by ")
                    .color(NamedTextColor.RED)
                    .append(text(requiredTypeDisplayName, NamedTextColor.GRAY))
                    .append(text('!'))
                    .build();
            })
            .handler(NoPermissionException.class, (formatter, ctx) -> Components.parse(Lang.NO_PERMISSION))
            .decorator(c -> Components.parse(Lang.getHelp()).append(c))
            .registerTo(commandManager);
    }

    public void registerSubcommand(Function<Command.Builder<Commander>, Command.Builder<? extends Commander>> builderModifier) {
        commandManager.command(builderModifier.apply(rootBuilder()));
    }

    private Command.Builder<Commander> rootBuilder() {
        return commandManager.commandBuilder(
            squareMarker.config.commandLabel,
            Description.description("Squaremarker command. '/" + squareMarker.config.commandLabel + " help'"),
            squareMarker.config.commandAliases.toArray(new String[0])
        );
    }
}
