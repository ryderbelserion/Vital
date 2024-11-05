package com.ryderbelserion.vital.api.files.types;

import com.ryderbelserion.vital.api.files.CustomFile;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.File;

/**
 * Represents a custom YAML file for configuration.
 *
 * <p>This class extends {@link CustomFile} to provide specific functionality
 * for loading, saving, and manipulating YAML files using Configurate.
 *
 * @version 0.0.5
 * @since 0.0.5
 * @author ryderbelserion
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
     * @since 0.0.5
     */
    public YamlCustomFile(final File file) {
        super(file);

        this.loader = YamlConfigurationLoader.builder().indent(2).file(file).build();
    }

    /**
     * Loads the YAML configuration from the file.
     *
     * <p>If the file is a directory or loading fails, appropriate warnings are logged.
     *
     * @return the current instance of {@link YamlCustomFile}
     * @since 0.0.5
     */
    public final YamlCustomFile loadConfiguration() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        try {
            this.configurationNode = this.loader.load();
        } catch (ConfigurateException exception) {
            this.logger.warn("Cannot load configuration file: {}", getFileName(), exception);
        }

        return this;
    }

    /**
     * Saves the YAML configuration to the file.
     *
     * <p>If the file is a directory or the configuration node is null, appropriate warnings are logged.
     *
     * @return the current instance of {@link YamlCustomFile}
     * @since 0.0.5
     */
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

        try {
            this.loader.save(this.configurationNode);
        } catch (ConfigurateException exception) {
            this.logger.warn("Cannot save configuration file: {}", getFileName(), exception);
        }

        return this;
    }

    /**
     * Retrieves a string value from the configuration at the specified path.
     *
     * @param path the configuration path to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the string value at the specified path, or the default value if not found
     * @since 0.0.5
     */
    public final String getStringValue(final String path, final String defaultValue) {
        return this.configurationNode.node(path).getString(defaultValue);
    }

    /**
     * Gets the root configuration node of the YAML file.
     *
     * @return the root {@link CommentedConfigurationNode} of the configuration
     * @since 0.0.5
     */
    public final CommentedConfigurationNode getConfigurationNode() {
        return this.configurationNode;
    }

    /**
     * Returns the current instance of {@link YamlCustomFile}.
     *
     * @return the current custom YAML file instance
     * @since 0.0.5
     */
    @Override
    public final CustomFile<YamlCustomFile> getInstance() {
        return this;
    }

    /**
     * Checks whether the configuration node is not null, indicating the configuration is loaded.
     *
     * @return {@code true} if the configuration is loaded, {@code false} otherwise
     * @since 0.0.5
     */
    @Override
    public final boolean isConfigurationLoaded() {
        return this.configurationNode != null;
    }
}