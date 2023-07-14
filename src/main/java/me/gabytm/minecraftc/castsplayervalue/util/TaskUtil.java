package me.gabytm.minecraftc.castsplayervalue.util;

import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TaskUtil {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(CastsPlayerValuePlugin.class);

    public static void async(@NotNull final Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    public static void async(@NotNull final Runnable task, final long delay, final long period) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
    }

}
