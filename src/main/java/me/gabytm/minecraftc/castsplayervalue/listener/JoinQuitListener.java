package me.gabytm.minecraftc.castsplayervalue.listener;

import me.gabytm.minecraftc.castsplayervalue.value.PlayerValueManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class JoinQuitListener implements Listener {

    private final PlayerValueManager playerValueManager;

    public JoinQuitListener(@NotNull final PlayerValueManager playerValueManager) {
        this.playerValueManager = playerValueManager;
    }

    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent event) {
        playerValueManager.loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(@NotNull final PlayerQuitEvent event) {
        playerValueManager.unloadPlayer(event.getPlayer().getUniqueId());
    }

}
