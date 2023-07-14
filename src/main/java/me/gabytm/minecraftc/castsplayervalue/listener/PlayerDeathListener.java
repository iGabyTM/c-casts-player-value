package me.gabytm.minecraftc.castsplayervalue.listener;

import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import me.gabytm.minecraftc.castsplayervalue.util.Lang;
import me.gabytm.minecraftc.castsplayervalue.value.PlayerValueManager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathListener implements Listener {

    private final CastsPlayerValuePlugin plugin;
    private final PlayerValueManager valueManager;

    public PlayerDeathListener(@NotNull final CastsPlayerValuePlugin plugin) {
        this.plugin = plugin;
        this.valueManager = plugin.getPlayerValueManager();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEvent(@NotNull final PlayerDeathEvent event) {
        final var player = event.getPlayer();
        final var killer = player.getKiller();

        // The player was not killed by a player, and 'loseValueFromAllDeathCauses' is false
        if (killer == null && !plugin.config().loseValueFromAllDeathCauses()) {
            return;
        }

        final var playerValue = valueManager.getValue(player.getUniqueId());

        // The value should not be null for online players, but just in case it is!
        if (playerValue == null) {
            return;
        }

        // Subtract 1 if the player has any value left
        if (playerValue > 0) {
            valueManager.setValue(player.getUniqueId(), playerValue - 1);
            Lang.send(player, "value-lost", Placeholder.parsed("amount", String.valueOf(playerValue - 1)));
        }

        if (killer == null) {
            return;
        }

        final var killerValue = valueManager.getValue(killer.getUniqueId());

        // Again, should not be null
        if (killerValue == null) {
            return;
        }

        // Give value to the killer only if the killed player had any left
        if (playerValue != 0) {
            valueManager.setValue(killer.getUniqueId(), killerValue + 1);
            killer.sendRawMessage("<green>You have killed %s and got 1 value, total value: %d".formatted(player.getName(), killerValue + 1));
            Lang.send(killer, "value-gained", Placeholder.parsed("amount", String.valueOf(killerValue + 1)));
        }
    }

}
