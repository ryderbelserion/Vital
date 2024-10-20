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
 * @version 0.0.4
 * @since 0.0.1
 */
public interface VitalAPI {

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
     * Saves a single file to disk.
     *
     * @param fileName the name of the file
     * @param replace  true or false
     * @since 0.0.1
     */
    default void saveResource(@NotNull final String fileName, final boolean replace) {

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
}