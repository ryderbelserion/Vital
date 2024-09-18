package com.ryderbelserion.vital.paper.modules;

import com.ryderbelserion.vital.common.api.interfaces.IModule;
import com.ryderbelserion.vital.paper.modules.interfaces.IPaperModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * An event registry to register events
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public class EventRegistry {

    private final JavaPlugin plugin;

    /**
     * Builds the event registry
     * 
     * @param plugin {@link JavaPlugin}
     * @since 0.0.1
     */
    public EventRegistry(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a listener to the server
     *
     * @param event {@link IModule}
     * @since 0.0.1
     */
    public void addEvent(@NotNull final IPaperModule event) {
        this.plugin.getServer().getPluginManager().registerEvents(event, this.plugin);
    }
}