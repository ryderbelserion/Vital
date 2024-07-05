package com.ryderbelserion.vital.paper.plugins.interfaces;

import java.util.UUID;

public interface Plugin {

    default boolean isEnabled() { return false; }

    default String getName() { return ""; }

    default void add() {}

    default void remove() {}

    default boolean isVanished(final UUID uuid) { return false; }

}