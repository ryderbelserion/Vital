package com.ryderbelserion.vital.paper.plugins;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {

    public static Map<String, Plugin> plugins = new HashMap<>();

    private static final ComponentLogger logger = Vital.api().getLogger();
    private static final boolean isLogging = Vital.api().isLogging();

    public static void registerPlugin(@NotNull final Plugin plugin) {
        plugins.put(plugin.getName(), plugin);

        plugin.add();
    }

    public static Plugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    public static boolean isEnabled(@NotNull final String name) {
        return getPlugin(name).isEnabled();
    }

    public static void unregisterPlugin(@NotNull final Plugin plugin) {
        plugins.remove(plugin.getName());

        plugin.remove();
    }

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

    public static Map<String, Plugin> getPlugins() {
        return Collections.unmodifiableMap(plugins);
    }
}