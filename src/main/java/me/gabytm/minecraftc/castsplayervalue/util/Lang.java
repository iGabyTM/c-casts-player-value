package me.gabytm.minecraftc.castsplayervalue.util;

import me.gabytm.minecraftc.castsplayervalue.CastsPlayerValuePlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Lang {

    private static final CastsPlayerValuePlugin plugin = JavaPlugin.getPlugin(CastsPlayerValuePlugin.class);

    public static void send(
            @NotNull final Audience audience, @NotNull final String key,
            @Nullable final TagResolver tagResolver
    ) {
        final var message = plugin.config().messages().get(key);

        if (message == null || message.isEmpty()) {
            return;
        }

        final var mini = MiniMessage.miniMessage();

        if (tagResolver == null) {
            audience.sendMessage(mini.deserialize(message));
        } else {
            audience.sendMessage(mini.deserialize(message, tagResolver));
        }
    }

}
