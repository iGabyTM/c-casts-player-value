package me.gabytm.minecraftc.castsplayervalue.command.impl;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import me.gabytm.minecraftc.castsplayervalue.value.PlayerValueManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command("playervalue")
public class RemoveValueCommand {

    private final PlayerValueManager playerValueManager;

    public RemoveValueCommand(@NotNull final PlayerValueManager playerValueManager) {
        this.playerValueManager = playerValueManager;
    }

    private void removeValue(
            @NotNull final CommandSender sender, @NotNull final OfflinePlayer player,
            final int currentValue, final int toRemove
    ) {
        playerValueManager.setValue(player.getUniqueId(), currentValue - toRemove);
        sender.sendRichMessage("<green>Removed %d from %s, new value: %d".formatted(toRemove, player.getName(), currentValue - toRemove));
    }

    @Permission("castsplayervalue.admin")
    @Command("remove")
    public void onCommand(
            @NotNull final CommandSender sender, @NotNull final OfflinePlayer player,
            @NotNull final Integer toRemove
    ) {
        final var uuid = player.getUniqueId();
        final var currentValue = playerValueManager.getValue(uuid);

        // The player was not loaded, need to load it first
        if (currentValue == null) {
            playerValueManager.loadPlayer(uuid, value -> removeValue(sender, player, value, toRemove));
        } else {
            removeValue(sender, player, currentValue, toRemove);
        }
    }

}
