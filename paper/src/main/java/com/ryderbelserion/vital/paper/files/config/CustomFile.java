package com.ryderbelserion.vital.paper.files.config;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Builds a custom file to load with the File Manager.
 *
 * @author Ryder Belserion
 * @version 1.8.4
 * @since 1.0
 */
public class CustomFile {

    private @NotNull final Vital api = Vital.api();
    private @NotNull final ComponentLogger logger = this.api.getLogger();
    private final boolean isLogging = this.api.isLogging();

    private YamlConfiguration configuration = null;

    private final JavaPlugin plugin;
    private final File directory;

    private String strippedName = "";
    private String fileName = "";
    private File file = null;

    /**
     * A constructor to build a custom file.
     *
     * @param directory the directory
     */
    public CustomFile(@NotNull final JavaPlugin plugin, @NotNull final File directory) {
        this.directory = directory;
        this.plugin = plugin;
    }

    /**
     * Populates the data in the class.
     *
     * @param fileName the name of the file
     * @return {@link CustomFile}
     */
    public @Nullable final CustomFile apply(@NotNull final String fileName) {
        if (fileName.isEmpty()) {
            if (this.isLogging) {
                List.of(
                        "The file name cannot be empty!",
                        "File Name: " + fileName
                ).forEach(this.logger::error);
            }

            return null;
        }

        this.strippedName = fileName.replace(".yml", "");
        this.fileName = fileName;

        this.file = new File(this.directory, this.fileName);

        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                try {
                    if (isLogging) logger.info("Loading {}.yml...", strippedName);

                    configuration = YamlConfiguration.loadConfiguration(file);
                } catch (Exception exception) {
                    if (isLogging) logger.error("Failed to load or create {}.yml...", strippedName, exception);
                }
            }
        }.run(this.plugin);

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
    public final String getFileName() {
        return this.fileName;
    }

    /**
     * Gets the file configuration.
     *
     * @return the current configuration
     */
    public final YamlConfiguration getConfiguration() {
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
     * Gets the {@link File}.
     *
     * @return the {@link File}
     */
    public final File getFile() {
        return this.file;
    }

    /**
     * Saves a custom configuration.
     */
    public void save() {
        if (this.fileName.isEmpty() || !exists()) return;

        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                try {
                    configuration.save(file);
                } catch (IOException exception) {
                    if (isLogging) logger.error("Could not save {}.yml...", strippedName, exception);
                }
            }
        }.run(this.plugin);
    }

    /**
     * Reloads a custom configuration.
     */
    public void reload() {
        if (this.fileName.isEmpty() || !exists()) return;

        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                try {
                    configuration = YamlConfiguration.loadConfiguration(file);
                } catch (Exception exception) {
                    if (isLogging) logger.error("Could not reload the {}.yml...", strippedName, exception);
                }
            }
        }.run(this.plugin);
    }
}