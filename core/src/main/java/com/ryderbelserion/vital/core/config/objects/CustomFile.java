package com.ryderbelserion.vital.core.config.objects;

import com.ryderbelserion.vital.core.AbstractPlugin;
import com.ryderbelserion.vital.core.config.YamlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builds a custom file to load with the File Manager
 *
 * @author Ryder Belserion
 * @version 1.4
 * @since 1.0
 */
public class CustomFile {

    private @NotNull final AbstractPlugin api = AbstractPlugin.api();
    private final @NotNull Logger logger = this.api.getLogger();
    private final boolean isLogging = this.api.isLogging();

    private YamlFile configuration = null;

    private String strippedName = "";
    private String filePath = "";

    private final Path directory;

    /**
     * A constructor to build a custom file.
     *
     * @param directory the directory
     */
    public CustomFile(@NotNull final Path directory) {
        this.directory = directory;
    }

    /**
     * Populates the data in the class.
     *
     * @param filePath the name of the file
     * @return {@link CustomFile}
     */
    public @Nullable final CustomFile apply(@NotNull final String filePath) {
        if (filePath.isEmpty()) {
            List.of(
                    "The file path cannot be empty!",
                    "File Path: " + filePath
            ).forEach(this.logger::severe);

            return null;
        }

        int index = filePath.lastIndexOf("\\");

        this.strippedName = index != 1 ? filePath.substring(index + 1).replace(".yml", "") : filePath.replace(".yml", "");
        this.filePath = filePath;

        YamlFile file = new YamlFile(this.directory.resolve(this.filePath).toString());

        try {
            if (this.isLogging) this.logger.info("Loading " + this.strippedName + ".yml...");

            this.configuration = file;
            this.configuration.loadWithComments();
        } catch (Exception exception) {
            this.logger.log(Level.SEVERE, "Failed to load or create " + this.strippedName + ".yml...", exception);
        }

        return this;
    }

    /**
     * Gets the name of the file without the .yml extension.
     *
     * @return the name of the file without .yml
     */
    public final String getStrippedName() {
        return this.strippedName;
    }

    /**
     * Gets the file name.
     *
     * @return the name of the file
     */
    public final String getFilePath() {
        return this.filePath;
    }

    /**
     * Gets the file configuration.
     *
     * @return the current configuration
     */
    public final YamlFile getYamlFile() {
        return this.configuration;
    }

    /**
     * Checks if the configuration is null.
     *
     * @return true or false
     */
    public boolean exists() {
        return this.configuration != null;
    }

    /**
     * Saves a custom configuration.
     */
    public void save() {
        if (this.filePath.isEmpty()) return;

        if (!exists()) return;

        try {
            this.configuration.save();
        } catch (IOException exception) {
            this.logger.log(Level.SEVERE, "Could not save " + this.strippedName + ".yml...", exception);
        }
    }

    /**
     * Reloads a custom configuration.
     */
    public void reload() {
        if (this.filePath.isEmpty()) return;

        if (!exists()) return;

        try {
            this.configuration = new YamlFile(this.directory.resolve(this.filePath).toString());
            this.configuration.loadWithComments();
        } catch (Exception exception) {
            this.logger.log(Level.SEVERE, "Could not reload the " + this.strippedName + ".yml...", exception);
        }
    }
}