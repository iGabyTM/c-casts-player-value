package me.gabytm.minecraftc.castsplayervalue;

import me.gabytm.minecraftc.castsplayervalue.command.CommandLoader;
import me.gabytm.minecraftc.castsplayervalue.config.ConfigManager;
import me.gabytm.minecraftc.castsplayervalue.config.PluginConfig;
import me.gabytm.minecraftc.castsplayervalue.listener.JoinQuitListener;
import me.gabytm.minecraftc.castsplayervalue.storage.StorageManager;
import me.gabytm.minecraftc.castsplayervalue.value.PapiExpansion;
import me.gabytm.minecraftc.castsplayervalue.value.PlayerValueManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class CastsPlayerValuePlugin extends JavaPlugin {

    private ConfigManager configManager;
    private StorageManager storageManager;
    private PlayerValueManager playerValueManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        reload();
        this.storageManager = new StorageManager(this);
        this.playerValueManager = new PlayerValueManager(this);

        getServer().getPluginManager().registerEvents(new JoinQuitListener(getPlayerValueManager()), this);
        CommandLoader.load(this);

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiExpansion(this).register();
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void reload() {
        configManager.loadPluginConfig();
    }

    public PluginConfig config() {
        return configManager.getPluginConfig();
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public PlayerValueManager getPlayerValueManager() {
        return playerValueManager;
    }

}
