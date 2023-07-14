package me.gabytm.minecraftc.castsplayervalue.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {

    private final JavaPlugin plugin;
    private final Logger logger;

    private PluginConfig pluginConfig;

    public ConfigManager(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getSLF4JLogger();
    }

    @NotNull
    private YamlConfigurationLoader createLoader(@NotNull final Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .build();
    }

    public void loadPluginConfig() {
        final var path = plugin.getDataFolder().toPath().resolve("config.yml");

        if (!Files.exists(path)) {
            logger.info("Could not find config.yml, creating it...");
            plugin.saveDefaultConfig();
        }

        try {
            final var loader = createLoader(path);
            final var node = loader.load();
            this.pluginConfig = node.get(PluginConfig.class);

            if (pluginConfig == null) {
                logger.error("Could not load config.yml, some of the properties are either missing or invalid. Fix the config before attempting to use the plugin!");
            }
        } catch (ConfigurateException e) {
            logger.error("Could not load config.yml", e);
        }
    }

    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }

}
