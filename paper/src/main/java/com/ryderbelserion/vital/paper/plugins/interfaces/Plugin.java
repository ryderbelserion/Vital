package com.ryderbelserion.vital.paper.plugins.interfaces;

import org.jetbrains.annotations.NotNull;
import java.util.UUID;

/**
 * A plugin class used in {@link com.ryderbelserion.vital.paper.plugins.PluginManager}
 *
 * @author Ryder Belserion
 * @version 2.4.4
 * @since 2.0
 */
public interface Plugin {

    /**
     * Checks if the plugin is enabled.
     *
     * @return true or false
     */
    default boolean isEnabled() { return false; }

    /**
     * Gets the name of the plugin.
     *
     * @return plugin name
     */
    default @NotNull String getName() { return ""; }

    /**
     * Enable plugin
     */
    default void add() {}

    /**
     * Disable plugin
     */
    default void remove() {}

    /**
     * A generic check to see if a player is vanished.
     *
     * @param uuid player's uuid
     * @return true or false
     */
    default boolean isVanished(@NotNull final UUID uuid) { return false; }

}