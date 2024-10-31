package com.ryderbelserion.vital;

import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.interfaces.Plugin;
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

    private static final Vital instance = VitalProvider.get();
    private static final ComponentLogger logger = instance.getLogger();
    private static final boolean isVerbose = instance.isVerbose();

    private static final Map<String, Plugin> plugins = new HashMap<>();

    /**
     * A plugin manager handling plugin dependencies.
     *
     * @since 0.0.1
     */
    public PluginManager() {}

    /**
     * Registers a plugin on startup.
     *
     * @param plugin {@link Plugin}
     * @since 0.0.1
     */
    public static void registerPlugin(@NotNull final Plugin plugin) {
        plugins.put(plugin.getName(), plugin);

        plugin.init();
    }

    /**
     * Gets a plugin object.
     *
     * @param name the name of the plugin
     * @return {@link Plugin}
     * @since 0.0.1
     */
    public static @Nullable Plugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    /**
     * Checks if a plugin is enabled.
     *
     * @param name name of the plugin
     * @return true or false
     * @since 0.0.1
     */
    public static boolean isEnabled(@NotNull final String name) {
        final Plugin plugin = getPlugin(name);

        if (plugin == null) return false;

        return plugin.isEnabled();
    }

    /**
     * Unregisters a plugin.
     *
     * @param plugin {@link Plugin}
     * @since 0.0.1
     */
    public static void unregisterPlugin(@NotNull final Plugin plugin) {
        plugins.remove(plugin.getName());

        plugin.stop();
    }

    /**
     * Print all plugins and status to console.
     *
     * @since 0.0.1
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
     * Gets a map of available plugins.
     *
     * @return map of available plugins
     * @since 0.0.1
     */
    public static @NotNull Map<String, Plugin> getPlugins() {
        return Collections.unmodifiableMap(plugins);
    }
}