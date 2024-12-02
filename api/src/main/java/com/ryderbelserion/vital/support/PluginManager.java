package com.ryderbelserion.vital.support;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.support.interfaces.Plugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A manager for handling plugin dependencies within an application.
 * This class provides methods for registering, retrieving, and managing the lifecycle of plugins.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class PluginManager {

    private static final Vital instance = VitalProvider.get();
    private static final ComponentLogger logger = instance.getLogger();
    private static final boolean isVerbose = instance.isVerbose();

    private static final Map<String, Plugin> plugins = new HashMap<>();

    /**
     * An empty constructor that does nothing.
     *
     * @since 0.1.0
     */
    public PluginManager() {}

    /**
     * Registers a plugin on startup. This method adds the specified plugin to the internal map and initializes it.
     *
     * @param plugin the plugin to be registered and initialized
     * @since 0.1.0
     */
    public static void registerPlugin(@NotNull final Plugin plugin) {
        plugins.put(plugin.getName(), plugin.init());
    }

    /**
     * Retrieves a plugin by its name. This method returns the plugin associated with the specified name,
     * or null if no such plugin exists.
     *
     * @param name the name of the plugin to retrieve
     * @return the plugin associated with the specified name, or null if not found
     * @since 0.1.0
     */
    public static @Nullable Plugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    /**
     * Checks if a plugin is enabled. This method returns true if the plugin is registered and enabled,
     * otherwise false.
     *
     * @param name the name of the plugin to check
     * @return true if the plugin is enabled, false otherwise
     * @since 0.1.0
     */
    public static boolean isEnabled(@NotNull final String name) {
        final Plugin plugin = getPlugin(name);

        return plugin != null && plugin.isEnabled();
    }

    /**
     * Unregisters a plugin. This method removes the specified plugin from the internal map and stops it.
     *
     * @param plugin the plugin to be unregistered and stopped
     * @since 0.1.0
     */
    public static void unregisterPlugin(@NotNull final Plugin plugin) {
        plugins.remove(plugin.getName());

        plugin.stop();
    }

    /**
     * Prints the status of all registered plugins to the console. If the verbose mode is enabled,
     * it logs each plugin's name and its enabled status.
     *
     * @since 0.1.0
     */
    public static void printPlugins() {
        if (isVerbose) {
            getPlugins().forEach((name, plugin) -> {
                if (plugin.isEnabled() && !name.isEmpty()) {
                    logger.info("{}: FOUND", name);
                } else {
                    logger.info("{}: NOT FOUND", name);
                }
            });
        }
    }

    /**
     * Returns an unmodifiable map of all registered plugins. This method provides a read-only view
     * of the internal map of plugins.
     *
     * @return an unmodifiable map of all registered plugins
     * @since 0.1.0
     */
    public static @NotNull Map<String, Plugin> getPlugins() {
        return Collections.unmodifiableMap(plugins);
    }
}