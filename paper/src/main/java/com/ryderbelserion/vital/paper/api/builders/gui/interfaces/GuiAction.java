package com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import org.bukkit.event.Event;

/**
 * Executes actions on click.
 *
 * @param <T> the generic action
 *
 * @author Matt
 * @version 0.0.4
 * @since 0.0.1
 */
@FunctionalInterface
public interface GuiAction<T extends Event> {

    /**
     * Executes the event passed to it.
     *
     * @param event Inventory action
     * @since 0.0.1
     */
    void execute(final T event);

}