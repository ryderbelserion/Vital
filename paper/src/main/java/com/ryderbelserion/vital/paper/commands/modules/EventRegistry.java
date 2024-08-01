package com.ryderbelserion.vital.paper.commands.modules;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * A modular listener class to register/unregister events at runtime
 *
 * @author Ryder Belserion
 * @version 2.4.6
 * @since 2.4
 */
public class EventRegistry {

    private final JavaPlugin plugin;

    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Builds the event registry
     * 
     * @param plugin {@link JavaPlugin}
     */
    public EventRegistry(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a listener to the server
     *
     * @param listener {@link Listener}
     */
    public void addListener(@NotNull final Listener listener) {
        if (this.listeners.contains(listener)) return;

        this.listeners.add(listener);

        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    /**
     * Unregister a listener
     *
     * @param listener {@link Listener}
     */
    public void removeListener(@NotNull final Listener listener) {
        if (!this.listeners.contains(listener)) return;

        this.listeners.remove(listener);

        HandlerList.unregisterAll(listener);
    }
}