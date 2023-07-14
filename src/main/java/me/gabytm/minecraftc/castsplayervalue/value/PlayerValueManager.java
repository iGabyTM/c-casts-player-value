package me.gabytm.minecraftc.castsplayervalue.value;

import com.google.common.base.Preconditions;
import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import me.gabytm.minecraftc.castsplayervalue.storage.StorageManager;
import me.gabytm.minecraftc.castsplayervalue.util.TaskUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerValueManager {

    private final CastsPlayerValuePlugin plugin;
    private final StorageManager storageManager;

    private final Map<UUID, Integer> cache = new HashMap<>();
    private final List<Pair<UUID, Integer>> top = new ArrayList<>();

    public PlayerValueManager(@NotNull final CastsPlayerValuePlugin plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
        TaskUtil.async(this::refreshTop, 20, 20 * 60); // Refresh the top every 60 seconds
    }

    private void refreshTop() {
        final var temp = storageManager.getTop();
        top.clear();
        top.addAll(temp);
    }

    @Nullable
    public Pair<UUID, Integer> getTop(final int position) {
        Preconditions.checkArgument(position >= 1 && position <= 10, "Invalid top position " + position + ", expected a value between 1 and 10");
        return top.size() < position ? null : top.get(position - 1);
    }

    public void loadPlayer(@NotNull final UUID uuid, @Nullable final Consumer<Integer> callback) {
        if (Bukkit.isPrimaryThread()) {
            TaskUtil.async(() -> loadPlayer(uuid, callback));
            return;
        }

        storageManager.getValue(uuid)
                .whenComplete((value, throwable) -> {
                    cache.put(uuid, value);

                    if (callback != null) {
                        callback.accept(value);
                    }
                });
    }

    public void loadPlayer(@NotNull final UUID uuid) {
        loadPlayer(uuid, null);
    }

    public void unloadPlayer(@NotNull final UUID uuid) {
        cache.remove(uuid);
    }

    @Nullable
    public Integer getValue(@NotNull final UUID uuid) {
        return cache.get(uuid);
    }

    public void setValue(@NotNull final UUID uuid, final int value) {
        if (Bukkit.isPrimaryThread()) {
            TaskUtil.async(() -> setValue(uuid, value));
            return;
        }

        storageManager.updateValue(uuid, value);
        cache.put(uuid, value);
    }

}
