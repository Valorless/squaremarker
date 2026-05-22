package dev.sentix.squaremarker.command.commands;

import dev.sentix.squaremarker.Components;
import dev.sentix.squaremarker.SquareMarker;
import dev.sentix.squaremarker.command.Commander;
import dev.sentix.squaremarker.command.Commands;
import dev.sentix.squaremarker.command.SquaremarkerCommand;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.component.TypedCommandComponent;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.minecraft.extras.AudienceProvider;
import org.incendo.cloud.minecraft.extras.MinecraftHelp;
import org.incendo.cloud.minecraft.extras.RichDescription;
import org.incendo.cloud.suggestion.Suggestion;
import org.incendo.cloud.suggestion.SuggestionProvider;

import static org.incendo.cloud.parser.standard.StringParser.greedyStringParser;

public class HelpCommand extends SquaremarkerCommand {

    private final MinecraftHelp<Commander> help;

    public HelpCommand(SquareMarker plugin, Commands commands) {
        super(plugin, commands);
        this.help = createHelp();
    }

    @Override
    public void register() {
        TypedCommandComponent<Commander, String> helpQueryArgument = CommandComponent
            .<Commander, String>builder("query", greedyStringParser())
            .suggestionProvider(
                SuggestionProvider.blocking((context, input) ->
                    commands.commandManager
                        .createHelpHandler()
                        .queryRootIndex(context.sender())
                        .entries()
                        .stream()
                        .map(e -> Suggestion.suggestion(e.syntax()))
                        .toList()
                )
            )
            .optional()
            .build();

        commands.registerSubcommand(builder -> builder
            .literal("help")
            .argument(helpQueryArgument)
            .commandDescription(RichDescription.richDescription(Components.parse("Show marker help.")))
            .permission("squaremarker.help")
            .handler(this::execute)
        );
    }

    private void execute(CommandContext<Commander> context) {
        help.queryCommands(context.getOrDefault("query", ""), context.sender());
    }

    private MinecraftHelp<Commander> createHelp() {
        return MinecraftHelp.<Commander>builder()
            .commandManager(commands.commandManager)
            .audienceProvider(AudienceProvider.nativeAudience())
            .commandPrefix("/" + squareMarker.config.commandLabel + " help")
            .colors(
                MinecraftHelp.helpColors(
                    TextColor.color(0x5B00FF),
                    NamedTextColor.WHITE,
                    TextColor.color(0xC028FF),
                    NamedTextColor.GRAY,
                    NamedTextColor.DARK_GRAY
                )
            )
            .messages(MinecraftHelp.MESSAGE_HELP_TITLE, "squaremarker command help")
            .build();
    }
}
