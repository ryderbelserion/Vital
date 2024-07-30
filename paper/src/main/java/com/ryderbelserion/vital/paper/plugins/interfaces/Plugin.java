package com.ryderbelserion.vital.paper.plugins.interfaces;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Plugin {

    /**
     * @return true or false
     */
    default boolean isEnabled() { return false; }

    /**
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