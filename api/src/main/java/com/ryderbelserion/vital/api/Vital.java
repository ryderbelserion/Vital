package com.ryderbelserion.vital.api;

import com.ryderbelserion.vital.VitalProvider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;

/**
 * Vital's API
 *
 * @author ryderbelserion
 * @version 2.0.0
 * @since 2.0.0
 */
public interface Vital {

    /**
     * Starts the api.
     *
     * @since 0.0.1
     */
    default void start() {
        VitalProvider.register(this);
    }

    /**
     * Stops the api.
     *
     * @since 0.0.1
     */
    default void stop() {
        VitalProvider.unregister();
    }

    /**
     * Logs messages to console.
     *
     * @return true or false
     * @since 0.0.1
     */
    default boolean isVerbose() {
        return false;
    }

    /**
     * Gets the number format.
     *
     * @return #,###.##
     * @since 0.0.1
     */
    default String getNumberFormat() {
        return "#,###.##";
    }

    /**
     * Gets the rounding mode for the math methods.
     *
     * @return half_even
     * @since 0.0.1
     */
    default String getRounding() {
        return "half_even";
    }

    /**
     * Gets the plugin folder.
     *
     * @return the file
     * @since 0.0.1
     */
    default File getDataFolder() {
        return null;
    }

    /**
     * Gets the generic plugin folder.
     *
     * @return the file
     * @since 0.0.1
     */
    default File getDirectory() {
        return null;
    }

    /**
     * Gets the {@link F}.
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
    default void saveResource(final String fileName, final boolean replace) {

    }

    /**
     * Gets the name of the plugin.
     *
     * @return plugin name
     * @since 0.0.1
     */
    default String getPluginName() {
        return null;
    }

    /**
     * Gets the plugin's logger.
     *
     * @return the component logger
     * @since 0.0.1
     */
    default ComponentLogger getLogger() {
        return null;
    }
}