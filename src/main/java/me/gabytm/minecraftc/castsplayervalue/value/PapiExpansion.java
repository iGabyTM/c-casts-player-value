package me.gabytm.minecraftc.castsplayervalue.value;

import com.google.common.primitives.Ints;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PapiExpansion extends PlaceholderExpansion {

    private final CastsPlayerValuePlugin plugin;

    public PapiExpansion(@NotNull final CastsPlayerValuePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "playervalue";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return List.of(
                "%playervalue_value%",
                "%playervalue_top_name_#%",
                "%playervalue_top_value_#%"
        );
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.startsWith("top_")) {
            final var returnName = params.startsWith("top_name_");
            final var position = Ints.tryParse(params.split("_")[2]);

            if (position == null) {
                return null;
            }

            final var pair = plugin.getPlayerValueManager().getTop(position);

            if (pair == null) {
                return "N/A";
            }

            if (returnName) {
                return Optional.ofNullable(Bukkit.getOfflinePlayer(pair.getLeft()).getName())
                        .orElse("unknown");
            }

            return String.valueOf(pair.getRight());
        }

        if (player == null || !player.isOnline()) {
            return null;
        }

        return String.valueOf(plugin.getPlayerValueManager().getValue(player.getUniqueId()));
    }

}
