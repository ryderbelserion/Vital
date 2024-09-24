package com.ryderbelserion.vital.velocity.modules;

import com.ryderbelserion.vital.common.api.interfaces.IModule;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

/**
 * An event registry to register events
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public class EventRegistry {

    private final EventManager eventManager;
    private final PluginContainer container;

    /**
     * Creates a velocity scheduler
     *
     * @param container {@link PluginContainer}
     * @param server {@link ProxyServer}
     *
     * @since 0.0.1
     */
    public EventRegistry(@NotNull final PluginContainer container, @NotNull final ProxyServer server) {
        this.container = container;
        this.eventManager = server.getEventManager();
    }

    /**
     * Add an event to the server
     *
     * @param event {@link IModule}
     */
    public void addEvent(@NotNull IModule event) {
        this.eventManager.register(this.container, event);
    }
}