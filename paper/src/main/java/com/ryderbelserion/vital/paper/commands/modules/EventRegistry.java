package com.ryderbelserion.vital.paper.commands.modules;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

/**
 * A modular listener class to register/unregister events at runtime
 *
 * @author Ryder Belserion
 * @version 2.4.3
 * @since 2.4
 */
public class EventRegistry {

    private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(EventRegistry.class);

    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Empty constructor
     */
    public EventRegistry() {}

    /**
     * Add a listener to the server
     *
     * @param listener {@link Listener}
     */
    public void addListener(final Listener listener) {
        if (this.listeners.contains(listener)) return;

        this.listeners.add(listener);

        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }

    /**
     * Unregister a listener
     *
     * @param listener {@link Listener}
     */
    public void removeListener(final Listener listener) {
        if (!this.listeners.contains(listener)) return;

        this.listeners.remove(listener);

        HandlerList.unregisterAll(listener);
    }
}