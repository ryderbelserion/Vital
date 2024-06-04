package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.core.util.AdvUtil;
import com.ryderbelserion.vital.paper.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * A collection of random utilities.
 *
 * @author Ryder Belserion
 * @version 1.5.4
 * @since 1.0
 */
public class MiscUtil {

    private MiscUtil() {
        throw new AssertionError();
    }

    /**
     * Parse a {@link String} for {@link org.bukkit.command.ConsoleCommandSender}.
     *
     * @param value the {@link String} to convert
     * @return {@link Component}
     * @since 1.0
     */
    public static @NotNull Component parse(@NotNull final String value) {
        if (value.isEmpty()) return Component.empty();

        return parse(value, null);
    }

    /**
     * Send a {@link String} with {@link PlaceholderAPI} support.
     *
     * @param value the {@link String} to send
     * @param uuid the {@link UUID} of the {@link Player} to send the {@link String} to
     * @return {@link Component}
     * @since 1.0
     */
    public static @NotNull Component parse(@NotNull final String value, @Nullable final UUID uuid) {
        if (value.isEmpty()) return Component.empty();

        return uuid != null ? parse(value, null, Bukkit.getPlayer(uuid)) : parse(value, null, null);
    }

    /**
     * Parse placeholders in a {@link String} than return a {@link Component}.
     *
     * @param value the {@link String} to alter
     * @param placeholders the {@link Map} to use
     * @param player the {@link Player} to send the message to
     * @return {@link Component}
     * @since 1.0
     */
    public static @NotNull Component parse(@NotNull final String value, @Nullable Map<String, String> placeholders, @Nullable final Player player) {
        if (value.isEmpty()) return Component.empty();

        String clonedMessage = Support.placeholder_api.isEnabled() && player != null ? PlaceholderAPI.setPlaceholders(player, value) : value;

        return placeholders != null ? AdvUtil.parse(clonedMessage, placeholders) : AdvUtil.parse(clonedMessage);
    }
}