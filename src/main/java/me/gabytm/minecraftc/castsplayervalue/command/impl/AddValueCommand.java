package me.gabytm.minecraftc.castsplayervalue.command.impl;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import me.gabytm.minecraftc.castsplayervalue.value.PlayerValueManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command("playervalue")
public class AddValueCommand {

    private final PlayerValueManager playerValueManager;

    public AddValueCommand(@NotNull final PlayerValueManager playerValueManager) {
        this.playerValueManager = playerValueManager;
    }

    private void addValue(
            @NotNull final CommandSender sender, @NotNull final OfflinePlayer player,
            final int currentValue, final int toAdd
    ) {
        playerValueManager.setValue(player.getUniqueId(), currentValue + toAdd);
        sender.sendRichMessage("<green>Added %d to %s, new value: %d".formatted(toAdd, player.getName(), currentValue + toAdd));
    }

    @Permission("castsplayervalue.admin")
    @Command("add")
    public void onCommand(
            @NotNull final CommandSender sender, @NotNull final OfflinePlayer player,
            @NotNull final Integer toAdd
    ) {
        final var uuid = player.getUniqueId();
        final var currentValue = playerValueManager.getValue(uuid);

        // The player was not loaded, need to load it first
        if (currentValue == null) {
            playerValueManager.loadPlayer(uuid, value -> addValue(sender, player, value, toAdd));
        } else {
            addValue(sender, player, currentValue, toAdd);
        }
    }

}
