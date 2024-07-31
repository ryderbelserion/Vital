package com.ryderbelserion.vital.paper.plugins;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.files.config.CustomFile;
import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A plugin manager handling plugin dependencies.
 *
 * @author Ryder Belserion
 * @version 2.4
 * @since 2.0
 */
public class PluginManager {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(CustomFile.class);
    private static final ComponentLogger logger = plugin.getComponentLogger();
    private static final boolean isLogging = Vital.api().isLogging();

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
    public static @NotNull Plugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    /**
     * Checks if a plugin is enabled.
     *
     * @param name name of the plugin
     * @return true or false
     */
    public static boolean isEnabled(@NotNull final String name) {
        return getPlugin(name).isEnabled();
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