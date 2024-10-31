package com.ryderbelserion.vital.api;

import com.google.gson.GsonBuilder;
import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.config.ConfigManager;
import com.ryderbelserion.vital.config.beans.Plugin;
import com.ryderbelserion.vital.config.keys.ConfigKeys;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Vital's API
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public interface Vital {

    /**
     * Starts the api.
     *
     * @since 0.0.1
     */
    default void start() {
        VitalProvider.register(this);

        ConfigManager.load();
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
     * Reloads the api.
     *
     * @since 0.0.1
     */
    default void reload() {
        ConfigManager.reload();
    }

    /**
     * Gets the library's plugin settings
     *
     * @return {@link Plugin}
     */
    default Plugin getPlugin() {
        return ConfigManager.getConfig().getProperty(ConfigKeys.settings);
    }

    /**
     * Logs messages to console.
     *
     * @return true or false
     * @since 0.0.1
     */
    default boolean isVerbose() {
        return getPlugin().isVerbose();
    }

    /**
     * Gets the number format.
     *
     * @return #,###.##
     * @since 0.0.1
     */
    default String getNumberFormat() {
        return getPlugin().getNumberFormat();
    }

    /**
     * Gets the rounding mode for the math methods.
     *
     * @return half_even
     * @since 0.0.1
     */
    default String getRounding() {
        return getPlugin().getRounding();
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
     * Parses a value with platform specific placeholder handling!
     *
     * @param audience sender
     * @param value the value to parse
     * @param placeholders map of placeholders
     * @return the parsed string
     * @since 0.0.4
     */
    @NotNull String placeholders(@NotNull final Audience audience, @NotNull final String value, @NotNull final Map<String, String> placeholders);

    /**
     * Colors a bit of text, with placeholder parsing!
     *
     * @param audience sender
     * @param value the value to parse
     * @param placeholders map of placeholders
     * @return the parsed string
     * @since 0.0.4
     */
    @NotNull Component color(@NotNull final Audience audience, @NotNull final String value, @NotNull final Map<String, String> placeholders);

    /**
     * Sends a message to a player.
     *
     * @param audience the player
     * @param value the value to parse
     * @param placeholders the placeholders
     * @since 0.0.4
     */
    void sendMessage(@NotNull final Audience audience, @NotNull final String value, @NotNull final Map<String, String> placeholders);

    /**
     * Gets the generic plugin folder.
     *
     * @return the file
     * @since 0.0.1
     */
    default File getPluginsFolder() {
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
     * @param resourcePath the name of the file
     * @param replace true or false
     * @since 0.0.1
     */
    default void saveResource(String resourcePath, final boolean replace) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');

        final InputStream inputStream = getResource(resourcePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found.");
        }

        File outFile = new File(getDataFolder(), resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(getDataFolder(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                final OutputStream outputStream = new FileOutputStream(outFile);

                byte[] buf = new byte[1024];
                int len;

                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }

                outputStream.close();
                inputStream.close();
            } else {
                getLogger().warn("Could not save {} to {} because {} already exists", outFile.getName(), outFile, outFile.getName());
            }
        } catch (IOException exception) {
            getLogger().error("Could not save {} to {}", outFile.getName(), outDir, exception);
        }
    }

    /**
     * Gets a resource from src/main/resources
     *
     * @param filename the file name
     * @return input stream
     * @since 2.0.0
     */
    default InputStream getResource(@NotNull String filename) {
        try {
            URL url = getClass().getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();

            connection.setUseCaches(false);

            return connection.getInputStream();
        } catch (IOException exception) {
            return null;
        }
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

    /**
     * Gets the gson builder.
     *
     * @return {@link GsonBuilder}
     * @since 0.0.1
     */
    default GsonBuilder getGson() {
        return null;
    }
}