package com.ryderbelserion.vital.api.interfaces;

import com.ryderbelserion.vital.PluginManager;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

/**
 * A plugin class used in {@link PluginManager}
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public interface Plugin {

    /**
     * Checks if the plugin is enabled.
     *
     * @return true or false
     * @since 0.0.1
     */
    default boolean isEnabled() { 
        return false; 
    }

    /**
     * Gets the name of the plugin.
     *
     * @return plugin name
     * @since 0.0.1
     */
    default @NotNull String getName() { 
        return ""; 
    }

    /**
     * Enable plugin
     * @since 0.0.1
     */
    default void init() {}

    /**
     * Disable plugin
     * @since 0.0.1
     */
    default void stop() {}

    /**
     * A generic check to see if a player is vanished.
     *
     * @param uuid player's uuid
     * @return true or false
     * @since 0.0.1
     */
    default boolean isVanished(@NotNull final UUID uuid) { 
        return false; 
    }

    /**
     * A generic check to see if a player is ignoring the sender.
     *
     * @param sender sender's uuid
     * @param target player's uuid
     * @return true or false
     * @since 0.0.1
     */
    default boolean isIgnored(@NotNull final UUID sender, @NotNull final UUID target) {
        return false;
    }

    /**
     * A generic check to see if a player is muted.
     *
     * @param uuid player's uuid
     * @return true or false
     * @since 0.0.1
     */
    default boolean isMuted(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * A generic check to see if a player is afk.
     *
     * @param uuid player's uuid
     * @return true or false
     * @since 0.0.1
     */
    default boolean isAfk(@NotNull final UUID uuid) {
        return false;
    }

    /**
     * A generic getter
     *
     * @param <T> anything we want
     * @return T
     * @since 0.0.1
     */
    default <T> T get() {
        return null;
    }
}