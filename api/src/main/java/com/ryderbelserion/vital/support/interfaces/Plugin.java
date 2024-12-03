package com.ryderbelserion.vital.support.interfaces;

import com.ryderbelserion.vital.support.PluginManager;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

/**
 * A plugin class used in {@link PluginManager}.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Plugin {

    /**
     * Checks if the plugin is enabled.
     *
     * @return true or false
     * @since 0.1.0
     */
    default boolean isEnabled() {
        return false;
    }

    /**
     * Gets the name of the plugin.
     *
     * @return the plugin name
     * @since 0.1.0
     */
    default @NotNull String getName() {
        return "";
    }

    /**
     * Enable plugin.
     *
     * @return {@link Plugin}
     * @since 0.1.0
     */
    default Plugin init() {
        return this;
    }

    /**
     * Disable plugin.
     *
     * @since 0.1.0
     */
    default void stop() {}

    /**
     * Checks if a player is vanished.
     *
     * @param uuid the player's UUID
     * @return true or false
     * @since 0.1.0
     */
    default boolean isVanished(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Checks if a player is ignoring the sender.
     *
     * @param sender the sender's UUID
     * @param target the player's UUID
     * @return true or false
     * @since 0.1.0
     */
    default boolean isIgnored(@NotNull final UUID sender, @NotNull final UUID target) {
        return false;
    }

    /**
     * Checks if a player is muted.
     *
     * @param uuid the player's UUID
     * @return true or false
     * @since 0.1.0
     */
    default boolean isMuted(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * Checks if a player is AFK.
     *
     * @param uuid the player's UUID
     * @return true or false
     * @since 0.1.0
     */
    default boolean isAfk(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * A generic getter.
     *
     * @param <T> the type
     * @return the value
     * @since 0.1.0
     */
    default <T> T get() {
        return null;
    }
}