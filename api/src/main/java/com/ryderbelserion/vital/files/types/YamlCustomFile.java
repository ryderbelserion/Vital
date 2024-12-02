package com.ryderbelserion.vital.files.types;

import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.files.CustomFile;
import com.ryderbelserion.vital.files.enums.FileType;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a custom YAML file for configuration.
 *
 * <p>This class extends {@link CustomFile} to provide specific functionality
 * for loading, saving, and manipulating YAML files using Configurate.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class YamlCustomFile extends CustomFile<YamlCustomFile> {

    private final YamlConfigurationLoader loader;
    private CommentedConfigurationNode configurationNode;

    /**
     * Constructs a new {@link YamlCustomFile} instance.
     *
     * <p>This constructor initializes the YAML configuration loader
     * with specific file and settings.
     *
     * @param file the file object to be wrapped by this custom YAML file
     * @param isDynamic whether the custom file should be treated as dynamic
     * @since 0.1.0
     */
    public YamlCustomFile(final File file, final boolean isDynamic) {
        super(file, isDynamic);

        this.loader = YamlConfigurationLoader.builder().indent(2).file(file).build();
    }

    /**
     * Retrieves the file type of this custom file.
     *
     * @return the file type, which is {@link FileType#YAML}
     * @since 0.1.0
     */
    @Override
    public final FileType getFileType() {
        return FileType.YAML;
    }

    /**
     * Loads the YAML configuration from the file.
     *
     * <p>If the file is a directory or loading fails, appropriate warnings are logged.
     *
     * @return the current instance of {@link YamlCustomFile}
     * @since 0.1.0
     */
    @Override
    public final YamlCustomFile loadConfiguration() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        this.configurationNode = CompletableFuture.supplyAsync(() -> {
            try {
                return this.loader.load();
            } catch (ConfigurateException exception) {
                this.logger.warn("Cannot load configuration file: {}", getFileName(), exception);
            }

            return null;
        }).join();

        return this;
    }

    /**
     * Saves the YAML configuration to the file.
     *
     * <p>If the file is a directory or the configuration node is null, appropriate warnings are logged.
     *
     * @return the current instance of {@link YamlCustomFile}
     * @since 0.1.0
     */
    @Override
    public final YamlCustomFile saveConfiguration() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        if (this.configurationNode == null) {
            if (this.isVerbose) {
                this.logger.error("Configuration is null, cannot save {}!", getFileName());
            }

            return this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.loader.save(this.configurationNode);
            } catch (ConfigurateException exception) {
                this.logger.warn("Cannot save configuration file: {}", getFileName(), exception);
            }
        });

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @param defaultValue {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final String getStringValueWithDefault(final String defaultValue, final Object... path) {
        return this.configurationNode.node(path).getString(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final String getStringValue(final Object... path) {
        return getStringValueWithDefault("", path);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @param defaultValue {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean getBooleanValueWithDefault(final boolean defaultValue, final Object... path) {
        return this.configurationNode.node(path).getBoolean(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean getBooleanValue(final Object... path) {
        return getBooleanValueWithDefault(Boolean.FALSE, path);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @param defaultValue {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final double getDoubleValueWithDefault(final double defaultValue, final Object... path) {
        return this.configurationNode.node(path).getDouble(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final double getDoubleValue(final Object... path) {
        return getDoubleValueWithDefault(Double.NaN, path);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @param defaultValue {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final long getLongValueWithDefault(final long defaultValue, final Object... path) {
        return this.configurationNode.node(path).getLong(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final long getLongValue(final Object... path) {
        return getLongValueWithDefault(Long.MIN_VALUE, path);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @param defaultValue {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final int getIntValueWithDefault(final int defaultValue, final Object... path) {
        return this.configurationNode.node(path).getInt(defaultValue);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final int getIntValue(final Object... path) {
        return getIntValueWithDefault(Integer.MIN_VALUE, path);
    }

    /**
     * {@inheritDoc}
     *
     * @param path {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final List<String> getStringList(final Object... path) {
        try {
            return this.configurationNode.node(path).getList(String.class);
        } catch (SerializationException exception) {
            throw new GenericException("Failed to serialize " + Arrays.toString(path), exception);
        }
    }

    /**
     * Gets the root configuration node of the YAML file.
     *
     * @return the root {@link CommentedConfigurationNode} of the configuration
     * @since 0.1.0
     */
    public final CommentedConfigurationNode getConfigurationNode() {
        return this.configurationNode;
    }

    /**
     * Returns the current instance of {@link YamlCustomFile}.
     *
     * @return the current custom YAML file instance
     * @since 0.1.0
     */
    @Override
    public final CustomFile<YamlCustomFile> getInstance() {
        return this;
    }

    /**
     * Checks whether the configuration node is not null, indicating the configuration is loaded.
     *
     * @return {@code true} if the configuration is loaded, {@code false} otherwise
     * @since 0.1.0
     */
    @Override
    public final boolean isConfigurationLoaded() {
        return this.configurationNode != null;
    }
}