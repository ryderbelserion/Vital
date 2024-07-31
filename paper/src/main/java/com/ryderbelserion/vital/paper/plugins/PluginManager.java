package com.ryderbelserion.vital.paper.plugins;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A plugin manager handling plugin dependencies.
 *
 * @author Ryder Belserion
 * @version 2.4.5
 * @since 2.0
 */
public class PluginManager {

    private static final Vital api = Vital.api();
    private static final ComponentLogger logger = api.getLogger();
    private static final boolean isLogging = api.isLogging();

    private static final Map<String, Plugin> plugins = new HashMap<>();

    /**
     * Empty constructor
     */
    public PluginManager() {}

    /**
     * Registers a plugin on startup.
     *
     * @param plugin {@link Plugin}
     */
    public static void registerPlugin(@NotNull final Plugin plugin) {
        plugins.put(plugin.getName(), plugin);

        plugin.add();
    }

    /**
     * Gets a plugin object.
     *
     * @param name the name of the plugin
     * @return {@link Plugin}
     */
    public static @Nullable Plugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    /**
     * Checks if a plugin is enabled.
     *
     * @param name name of the plugin
     * @return true or false
     */
    public static boolean isEnabled(@NotNull final String name) {
        final Plugin plugin = getPlugin(name);

        if (plugin == null) return false;

        return plugin.isEnabled();
    }

    /**
     * Unregisters a plugin
     *
     * @param plugin {@link Plugin}
     */
    public static void unregisterPlugin(@NotNull final Plugin plugin) {
        plugins.remove(plugin.getName());

        plugin.remove();
    }

    /**
     * Print all plugins and status to console
     */
    public static void printPlugins() {
        if (isLogging) {
            getPlugins().forEach((name, plugin) -> {
                if (plugin.isEnabled() && !name.isEmpty()) {
                    logger.info("{}: FOUND", name);

                    return;
                }

                logger.info("{}: NOT FOUND", name);
            });
        }
    }

    /**
     * Gets a map of available plugins
     *
     * @return map of available plugins
     */
    public static @NotNull Map<String, Plugin> getPlugins() {
        return Collections.unmodifiableMap(plugins);
    }
}