package me.gabytm.minecraftc.castsplayervalue.command.impl;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Command("playervalue")
public class ReloadCommand {

    private final CastsPlayerValuePlugin plugin;

    public ReloadCommand(@NotNull final CastsPlayerValuePlugin plugin) {
        this.plugin = plugin;
    }

    @Permission("castsplayervalue.admin")
    @Command("reload")
    public void onCommand(@NotNull final CommandSender sender) {
        plugin.reload();
        sender.sendRichMessage("<green>Plugin reloaded!");
    }

}
