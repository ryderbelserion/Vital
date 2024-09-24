package com.ryderbelserion.vital.common.api.managers;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.common.api.Provider;
import com.ryderbelserion.vital.common.api.interfaces.IPlugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A plugin manager handling plugin dependencies.
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public class PluginManager {

    private static final VitalAPI api = Provider.getApi();
    private static final ComponentLogger logger = api.getComponentLogger();
    private static final boolean isVerbose = api.isVerbose();

    private static final Map<String, IPlugin> plugins = new HashMap<>();

    /**
     * A plugin manager handling plugin dependencies.
     * @since 0.0.1
     */
    public PluginManager() {}

    /**
     * Registers a plugin on startup.
     *
     * @param plugin {@link IPlugin}
     */
    public static void registerPlugin(@NotNull final IPlugin plugin) {
        plugins.put(plugin.getName(), plugin);

        plugin.init();
    }

    /**
     * Gets a plugin object.
     *
     * @param name the name of the plugin
     * @return {@link IPlugin}
     */
    public static @Nullable IPlugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    /**
     * Checks if a plugin is enabled.
     *
     * @param name name of the plugin
     * @return true or false
     */
    public static boolean isEnabled(@NotNull final String name) {
        final IPlugin plugin = getPlugin(name);

        if (plugin == null) return false;

        return plugin.isEnabled();
    }

    /**
     * Unregisters a plugin
     *
     * @param plugin {@link IPlugin}
     */
    public static void unregisterPlugin(@NotNull final IPlugin plugin) {
        plugins.remove(plugin.getName());

        plugin.stop();
    }

    /**
     * Print all plugins and status to console
     */
    public static void printPlugins() {
        if (isVerbose) {
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
    public static @NotNull Map<String, IPlugin> getPlugins() {
        return Collections.unmodifiableMap(plugins);
    }
}