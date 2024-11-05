package com.ryderbelserion.vital.api.files.types;

import com.ryderbelserion.vital.api.files.CustomFile;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import java.io.File;

public class YamlCustomFile extends CustomFile<YamlCustomFile> {

    private final YamlConfigurationLoader loader;

    /**
     * A constructor to build a custom yaml file.
     *
     * @param file {@link File}
     * @since 1.0.0
     */
    public YamlCustomFile(final File file) {
        super(file);

        this.loader = YamlConfigurationLoader.builder().indent(2).file(file).build();
    }

    private CommentedConfigurationNode node;

    /**
     * Load the custom file.
     *
     * @return {@link YamlCustomFile}
     * @since 0.0.1
     */
    public final YamlCustomFile load() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load, as it is a directory.");
            }

            return this;
        }

        try {
            this.node = this.loader.load();
        } catch (ConfigurateException exception) {
            this.logger.warn("Cannot load configuration file: {}", getFile(), exception);
        }

        return this;
    }

    /**
     * Saves the custom file.
     *
     * @return {@link YamlCustomFile}
     * @since 0.0.1
     */
    public final YamlCustomFile save() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save, as it is a directory.");
            }

            return this;
        }

        if (this.node == null) {
            if (this.isVerbose) {
                this.logger.error("File configuration is null, cannot save!");
            }

            return this;
        }

        try {
            this.loader.save(this.node);
        } catch (ConfigurateException exception) {
            this.logger.warn("Cannot save configuration file: {}", getFile(), exception);
        }

        return this;
    }

    /**
     * Gets the {@link CommentedConfigurationNode}.
     *
     * @return {@link CommentedConfigurationNode}
     * @since 0.0.1
     */
    public final CommentedConfigurationNode getNode() {
        return this.node;
    }

    /**
     * Get the yaml custom instance.
     *
     * @return the yaml custom instance
     * @since 1.0.0
     */
    @Override
    public final CustomFile<YamlCustomFile> getCustomFile() {
        return this;
    }

    /**
     * Checks if {@link CommentedConfigurationNode} is null.
     *
     * @return true or false
     * @since 0.0.1
     */
    @Override
    public final boolean exists() {
        return getNode() != null;
    }
}