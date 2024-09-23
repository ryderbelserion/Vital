package com.ryderbelserion.vital.common;

import com.google.gson.GsonBuilder;
import com.ryderbelserion.vital.common.api.Provider;
import com.ryderbelserion.vital.common.api.interfaces.IScheduler;
import com.ryderbelserion.vital.common.config.ConfigManager;
import com.ryderbelserion.vital.common.config.beans.Plugin;
import com.ryderbelserion.vital.common.config.keys.ConfigKeys;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.lang.reflect.Field;

/**
 * An interface to extend in each platform.
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public interface VitalAPI {

    /**
     * Initializes the plugin.
     *
     * @since 0.0.1
     */
    default void start() {
        try {
            Field api = Provider.class.getDeclaredField("api");
            api.setAccessible(true);
            api.set(null, this);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }

        ConfigManager.load();
    }

    /**
     * Reloads the api.
     *
     * @since 0.0.1
     */
    default void reload() {
        ConfigManager.reload();
    }

    /**
     * Stops the plugin.
     *
     * @since 0.0.1
     */
    default void stop() {
        ConfigManager.reload();
    }

    /**
     * Whether to use MiniMessage or not.
     *
     * @return true or false
     * @since 0.0.1
     */
    default boolean isVerbose() {
        return getPluginData().verbose;
    }

    /**
     * Gets the {@link F}
     *
     * @return {@link F}
     * @param <F> the file manager
     * @since 0.0.1
     */
    default <F> F getFileManager() {
        return null;
    }

    /**
     * Gets the mods directory.
     *
     * @return {@link File}
     * @since 0.0.1
     */
    default File getModsDirectory() {
        return null;
    }

    /**
     * Gets the root directory.
     *
     * @return {@link File}
     * @since 0.0.1
     */
    default File getDirectory() {
        return null;
    }

    /**
     * Saves a single file to disk.
     *
     * @param fileName the name of the file
     * @param replace  true or false
     * @since 0.0.1
     */
    default void saveResource(@NotNull final String fileName, final boolean replace) {

    }

    /**
     * Gets the {@link ComponentLogger}.
     *
     * @return {@link ComponentLogger}
     * @since 0.0.1
     */
    default ComponentLogger getComponentLogger() {
        return null;
    }

    /**
     * Gets the scheduler for the server
     *
     * @return {@link IScheduler}
     * @since 0.0.1
     */
    default IScheduler getScheduler() {
        return null;
    }

    /**
     * Gets the name of the plugin
     *
     * @return plugin name
     * @since 0.0.1
     */
    default String getPluginName() {
        return null;
    }

    /**
     * Gets the {@link GsonBuilder}
     *
     * @return {@link GsonBuilder}
     * @since 0.0.1
     */
    default GsonBuilder getBuilder() {
        return new GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization();
    }

    /**
     * Gets the {@link Plugin}
     *
     * @return {@link Plugin}
     * @since 0.0.1
     */
    default Plugin getPluginData() {
        return ConfigManager.getConfig().getProperty(ConfigKeys.settings);
    }
}