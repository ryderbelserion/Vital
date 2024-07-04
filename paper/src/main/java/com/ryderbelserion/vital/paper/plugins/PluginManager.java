package com.ryderbelserion.vital.paper.plugins;

import com.ryderbelserion.vital.paper.plugins.interfaces.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {

    public static Map<String, Plugin> plugins = new HashMap<>();

    public static void registerPlugin(@NotNull final Plugin plugin) {
        plugins.put(plugin.getName(), plugin);
    }

    public static Plugin getPlugin(@NotNull final String name) {
        return plugins.get(name);
    }

    public static boolean isEnabled(@NotNull final String name) {
        return getPlugin(name).isEnabled();
    }

    public static void unregisterPlugin(@NotNull final Plugin plugin) {
        plugins.remove(plugin.getName());
    }

    public static Map<String, Plugin> getPlugins() {
        return Collections.unmodifiableMap(plugins);
    }
}