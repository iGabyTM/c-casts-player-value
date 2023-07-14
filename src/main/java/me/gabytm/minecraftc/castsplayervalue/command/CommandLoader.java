package me.gabytm.minecraftc.castsplayervalue.command;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.message.MessageResolver;
import dev.triumphteam.cmd.core.message.context.MessageContext;
import dev.triumphteam.cmd.core.message.context.SyntaxMessageContext;
import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import me.gabytm.minecraftc.castsplayervalue.command.impl.AddValueCommand;
import me.gabytm.minecraftc.castsplayervalue.command.impl.ReloadCommand;
import me.gabytm.minecraftc.castsplayervalue.command.impl.RemoveValueCommand;
import me.gabytm.minecraftc.castsplayervalue.command.impl.SetValueCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandLoader {

    public static void load(@NotNull final CastsPlayerValuePlugin plugin) {
        final var manager = BukkitCommandManager.create(plugin);

        manager.registerArgument(OfflinePlayer.class, (sender, arg) -> {
            final var player = Bukkit.getOfflinePlayer(arg);
            return player.getFirstPlayed() == 0 ? null : player;
        });
        // Suggest only online players
        manager.registerSuggestion(OfflinePlayer.class, (sender, arg) ->
                Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .toList()
        );

        final MessageResolver<CommandSender, MessageContext> syntaxMessage = (sender, context) -> {
            final var ctx = (SyntaxMessageContext) context;
            final var syntax = ctx.getSyntax();
            sender.sendRichMessage("<red>Invalid syntax. Use <white>" + syntax);
        };

        manager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, syntaxMessage);
        manager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, syntaxMessage);

        manager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> {
            if (OfflinePlayer.class.isAssignableFrom(context.getArgumentType())) {
                sender.sendRichMessage("<red>Unknown player '" + context.getInvalidInput() + "'.");
                return;
            }

            if (Number.class.isAssignableFrom(context.getArgumentType())) {
                sender.sendRichMessage("<red>Invalid number '" + context.getInvalidInput() + "'.");
                return;
            }

            sender.sendRichMessage("<red>Invalid value '%s' for argument '%s', expected value of type %s".formatted(context.getInvalidInput(), context.getArgumentName(), context.getArgumentType().getSimpleName()));
        });

        manager.registerCommand(
                new ReloadCommand(plugin),
                new AddValueCommand(plugin.getPlayerValueManager()),
                new RemoveValueCommand(plugin.getPlayerValueManager()),
                new SetValueCommand(plugin.getPlayerValueManager())
        );
    }

}
