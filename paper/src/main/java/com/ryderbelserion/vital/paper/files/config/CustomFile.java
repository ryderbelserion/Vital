package com.ryderbelserion.vital.paper.files.config;

import com.ryderbelserion.vital.core.Vital;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Builds a custom file to load with the File Manager.
 *
 * @author Ryder Belserion
 * @version 2.4.4
 * @since 1.0
 */
public class CustomFile {

    private final Vital api = Vital.api();
    private final ComponentLogger logger = this.api.getLogger();
    private final boolean isLogging = this.api.isLogging();

    private YamlConfiguration configuration = null;

    private final File directory;

    private String strippedName = "";
    private String fileName = "";
    private File file = null;

    /**
     * A constructor to build a custom file.
     *
     * @param directory the directory
     */
    public CustomFile(@NotNull final File directory) {
        this.directory = directory;
    }

    /**
     * Populates the data in the class.
     *
     * @param fileName the name of the file
     * @return {@link CustomFile}
     */
    public @NotNull final CustomFile apply(@NotNull final String fileName) {
        if (fileName.isEmpty()) {
            if (this.isLogging) {
                List.of(
                        "The file name cannot be empty!",
                        "File Name: " + fileName
                ).forEach(this.logger::error);
            }

            return this;
        }

        this.strippedName = fileName.replace(".yml", "");
        this.fileName = fileName;

        this.file = new File(this.directory, this.fileName);

        try {
            if (this.isLogging) this.logger.info("Loading {}...", this.fileName);

            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.file)).join();
        } catch (Exception exception) {
            if (this.isLogging) this.logger.error("Failed to load or create {}...", this.fileName, exception);
        }

        return this;
    }

    /**
     * Gets the name of the file without the .yml extension.
     *
     * @return the name of the file without .yml
     */
    public @NotNull final String getStrippedName() {
        return this.strippedName;
    }

    /**
     * Gets the file name.
     *
     * @return the name of the file
     */
    public @NotNull final String getFileName() {
        return this.fileName;
    }

    /**
     * Gets the file configuration.
     *
     * @return the current configuration
     */
    public @NotNull final YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Checks if the configuration is null.
     *
     * @return true or false
     */
    public final boolean exists() {
        return this.configuration != null;
    }

    /**
     * Gets the {@link File}.
     *
     * @return the {@link File}
     */
    public @NotNull final File getFile() {
        return this.file;
    }

    /**
     * Saves a custom configuration.
     */
    public void save() {
        if (this.fileName.isEmpty() || !exists()) return;

        CompletableFuture.runAsync(() -> {
            try {
                this.configuration.save(this.file);
            } catch (Exception exception) {
                if (this.isLogging) this.logger.error("Failed to save: {}...", this.fileName, exception);
            }
        });
    }

    /**
     * Reloads a custom configuration.
     */
    public void reload() {
        if (this.fileName.isEmpty() || !exists()) return;

        try {
            if (this.isLogging) this.logger.info("Loading {}...", this.fileName);

            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.file)).join();
        } catch (Exception exception) {
            if (this.isLogging) this.logger.error("Could not reload the {}...", this.fileName, exception);
        }
    }
}