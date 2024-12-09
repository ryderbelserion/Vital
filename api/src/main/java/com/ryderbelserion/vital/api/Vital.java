package com.ryderbelserion.vital.api;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.exceptions.GenericException;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Vital's API.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class Vital {

    private YamlConfigurationLoader loader;
    private CommentedConfigurationNode config;

    /**
     * An empty constructor that does nothing.
     *
     * @since 0.1.0
     */
    public Vital() {}

    /**
     * Starts the API.
     *
     * @since 0.1.0
     */
    public void start() {
        VitalProvider.register(this);

        final File file = new File(getDataFolder(), "vital.yml");
        saveResource(file.getName(), false, false);

        this.loader = YamlConfigurationLoader.builder().indent(2).file(file).build();

        reload();
    }

    /**
     * Stops the API.
     *
     * @since 0.1.0
     */
    public void stop() {
        VitalProvider.unregister();
    }

    /**
     * Reloads the API.
     *
     * @since 0.1.0
     */
    public void reload() {
        this.config = CompletableFuture.supplyAsync(() -> {
            try {
                return this.loader.load();
            } catch (ConfigurateException exception) {
                getLogger().warn("Cannot load configuration file: vital.yml", exception);
            }
            return null;
        }).join();
    }

    /**
     * Logs messages to console.
     *
     * @return true or false
     * @since 0.1.0
     */
    public boolean isVerbose() {
        return this.config.node("settings", "is_verbose").getBoolean(false);
    }

    /**
     * Gets the number format.
     *
     * @return the number format
     * @since 0.1.0
     */
    public String getNumberFormat() {
        return this.config.node("settings", "number_format").getString("#,###.##");
    }

    /**
     * Gets the rounding mode.
     *
     * @return the rounding mode
     * @since 0.1.0
     */
    public String getRounding() {
        return this.config.node("settings", "rounding").getString("half_even");
    }

    /**
     * Gets the plugins folder on the server.
     *
     * @return the plugins folder
     * @since 0.1.0
     */
    public File getPluginsFolder() {
        return null;
    }

    /**
     * Gets the data folder for the plugin.
     *
     * @return the data folder
     * @since 0.1.0
     */
    public File getDataFolder() {
        return null;
    }

    /**
     * Gets the name of the plugin.
     *
     * @return the plugin name
     * @since 0.1.0
     */
    public String getPluginName() {
        return null;
    }

    /**
     * Gets the logger for the plugin.
     *
     * @return the {@link ComponentLogger}
     * @since 0.1.0
     */
    public ComponentLogger getLogger() {
        return null;
    }

    /**
     * Parses placeholders.
     *
     * @param line the value to parse
     * @return the parsed string
     * @since 0.1.0
     */
    public abstract @NotNull String placeholders(@NotNull final String line);

    /**
     * Parses placeholders.
     *
     * @param line the value to parse
     * @param placeholders the placeholders
     * @return the parsed string
     * @since 0.1.0
     */
    public abstract @NotNull String placeholders(@NotNull final String line, @NotNull final Map<String, String> placeholders);

    /**
     * Parses placeholders.
     *
     * @param audience the sender
     * @param line the value to parse
     * @param placeholders the placeholders
     * @return the parsed string
     * @since 0.1.0
     */
    public abstract @NotNull String placeholders(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    /**
     * Colors text with placeholders.
     *
     * @param line the value to parse
     * @return the parsed string
     * @since 0.1.0
     */
    public abstract @NotNull Component color(@NotNull final String line);

    /**
     * Colors text with placeholders.
     *
     * @param line the value to parse
     * @param placeholders the placeholders
     * @return the parsed string
     * @since 0.1.0
     */
    public abstract @NotNull Component color(@NotNull final String line, @NotNull final Map<String, String> placeholders);

    /**
     * Colors text with placeholders.
     *
     * @param audience the sender
     * @param line the value to parse
     * @param placeholders the placeholders
     * @return the parsed string
     * @since 0.1.0
     */
    public abstract @NotNull Component color(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    /**
     * Sends a message.
     *
     * @param audience the recipient
     * @param line the value to parse
     * @param placeholders the placeholders
     * @since 0.1.0
     */
    public abstract void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders);

    /**
     * Sends messages.
     *
     * @param audience the recipient
     * @param lines the values to parse
     * @param placeholders the placeholders
     * @since 0.1.0
     */
    public abstract void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders);

    /**
     * Chomps a message.
     *
     * @param message {@link String}
     * @return the chomped message
     * @since 0.2.0
     */
    public abstract String chomp(@NotNull final String message);

    /**
     * Saves a resource.
     *
     * @param resourcePath the file name
     * @param replace true or false
     * @param isVerbose true or false
     * @since 0.1.0
     */
    public void saveResource(String resourcePath, final boolean replace, final boolean isVerbose) {
        if (resourcePath == null || resourcePath.isEmpty()) {
            throw new GenericException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        final InputStream inputStream = getResource(resourcePath);

        if (inputStream == null) {
            throw new GenericException("The embedded resource '" + resourcePath + "' cannot be found.");
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
                if (isVerbose) getLogger().warn("Could not save {} to {} because {} already exists", outFile.getName(), outFile, outFile.getName());
            }
        } catch (IOException exception) {
            if (isVerbose) getLogger().error("Could not save {} to {}", outFile.getName(), outDir, exception);
        }
    }

    /**
     * Gets a resource.
     *
     * @param filename the file name
     * @return the input stream
     * @since 0.1.0
     */
    public InputStream getResource(@NotNull final String filename) {
        try {
            final URL url = getClass().getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            final URLConnection connection = url.openConnection();

            connection.setUseCaches(false);

            return connection.getInputStream();
        } catch (IOException exception) {
            return null;
        }
    }
}