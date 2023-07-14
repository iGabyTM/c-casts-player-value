package me.gabytm.minecraftc.castsplayervalue.command.impl;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import me.gabytm.minecraftc.castsplayervalue.value.PlayerValueManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command("playervalue")
public class SetValueCommand {

    private final PlayerValueManager playerValueManager;

    public SetValueCommand(@NotNull final PlayerValueManager playerValueManager) {
        this.playerValueManager = playerValueManager;
    }

    @Permission("castsplayervalue.admin")
    @Command("set")
    public void onCommand(
            @NotNull final CommandSender sender, @NotNull final OfflinePlayer player,
            @NotNull final Integer newValue
    ) {
        playerValueManager.setValue(player.getUniqueId(), newValue);
        sender.sendRichMessage("<green>Set %s's value to %d".formatted(player.getName(), newValue));
    }

}
