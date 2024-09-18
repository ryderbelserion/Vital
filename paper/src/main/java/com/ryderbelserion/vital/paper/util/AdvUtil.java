package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.paper.api.enums.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A collection of random utilities.
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public class AdvUtil {

    /**
     * Empty constructor
     *
     * @since 0.0.1
     */
    private AdvUtil() {
        throw new AssertionError();
    }

    /**
     * Send a {@link String} with {@link PlaceholderAPI} support.
     *
     * @param value the {@link String} to send
     * @param uuid the {@link UUID} of the {@link Player} to send the {@link String} to
     * @return {@link Component}
     * @since 0.0.1
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
     * @since 0.0.1
     */
    public static @NotNull Component parse(@NotNull final String value, @Nullable Map<String, String> placeholders, @Nullable final Player player) {
        if (value.isEmpty()) return Component.empty();

        return parse(parsePlaceholders(value, placeholders, player));
    }

    /**
     * Parse placeholders in a {@link String} than return a {@link String}
     *
     * @param value the message
     * @param placeholders the placeholders
     * @param player the player
     * @return the message
     */
    public static String parsePlaceholders(@NotNull String value, @Nullable Map<String, String> placeholders, @Nullable Player player) {
        String clonedMessage = Support.placeholder_api.isEnabled() && player != null ? PlaceholderAPI.setPlaceholders(player, value) : value;

        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey().toLowerCase();
                String entryValue = entry.getValue();

                clonedMessage = clonedMessage.replace(key, entryValue);
            }
        }

        return clonedMessage;
    }

    /**
     * Parses a message.
     *
     * @param message the {@link String} to alter
     * @return {@link Component}
     * @since 0.0.1
     */
    public static @NotNull Component parse(@NotNull final String message) {
        if (message.isEmpty()) return Component.empty();

        return MiniMessage.miniMessage().deserialize(message).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Converts a {@link String} to {@link Component}.
     *
     * @param component {@link String}
     * @return {@link Component}
     * @since 0.0.3
     */
    public static @NotNull Component toComponent(@NotNull final String component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
    }

    /**
     * Converts a lore to minimessage strings
     *
     * @param lore {@link List<Component>}
     * @return {@link List<Component>}
     * @since 0.0.3
     */
    public static @NotNull List<Component> toComponent(@NotNull final List<String> lore) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(toComponent(line)));
        }};
    }

    /**
     * Converts a lore to minimessage strings
     *
     * @param lore {@link List<Component>}
     * @return {@link List<Component>}
     * @since 0.0.3
     */
    public static @NotNull List<String> fromComponent(@NotNull final List<Component> lore) {
        return fromComponent(lore, false);
    }

    /**
     * Converts a lore to minimessage strings
     *
     * @param lore {@link List<Component>}
     * @param isMessage true or false
     * @return {@link List<Component>}
     * @since 0.0.3
     */
    public static @NotNull List<String> fromComponent(@NotNull final List<Component> lore, final boolean isMessage) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(fromComponent(line, isMessage)));
        }};
    }

    /**
     * Converts a {@link Component} to {@link String}.
     *
     * @param component {@link Component}
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String fromComponent(@NotNull final Component component) {
        return fromComponent(component, false);
    }

    /**
     * Converts a {@link Component} to {@link String}.
     *
     * @param component {@link Component}
     * @param isMessage true or false
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String fromComponent(@NotNull final Component component, final boolean isMessage) {
        final String value = MiniMessage.miniMessage().serialize(component);

        if (isMessage) {
            return value.replace("\\<", "<");
        }

        return value;
    }

    /**
     * Converts a {@link String} to a {@link Component} then a {@link String}.
     *
     * @param component {@link String}
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String convert(@NotNull final String component)  {
        return convert(component, false);
    }

    /**
     * Converts a {@link List<String>} to a {@link List<Component>} then a {@link List<String>}.
     *
     * @param components {@link List<String>}
     * @return {@link List<String>}
     * @since 0.0.3
     */
    public static @NotNull List<String> convert(@NotNull final List<String> components) {
        return convert(components, false);
    }

    /**
     * Converts a {@link List<String>} to a {@link List<Component>} then a {@link List<String>}.
     *
     * @param components {@link List<String>}
     * @param isMessage true or false
     * @return {@link List<String>}
     * @since 0.0.3
     */
    public static @NotNull List<String> convert(@NotNull final List<String> components, final boolean isMessage) {
        return new ArrayList<>(components.size()) {{
            components.forEach(line -> add(convert(line, isMessage)));
        }};
    }

    /**
     * Converts a {@link String} to a {@link Component} then a {@link String}.
     *
     * @param component {@link String}
     * @param isMessage true or false
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String convert(@NotNull final String component, final boolean isMessage)  {
        return fromComponent(toComponent(component), isMessage);
    }
}