package com.ryderbelserion.vital.discord.util.files;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

/**
 * Builds a custom file to load with the File Manager.
 *
 * @author Ryder Belserion
 * @version 1.8.4
 * @since 1.8
 */
public class CustomFile {

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
    public @Nullable final CustomFile apply(@NotNull final String fileName) {
        if (fileName.isEmpty()) return null;

        this.strippedName = fileName.replace(".yml", "");
        this.fileName = fileName;

        this.file = new File(this.directory, this.fileName);

        try {
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
        } catch (Exception exception) {
            exception.printStackTrace();
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
        if (this.fileName.isEmpty()) return;

        if (!exists()) return;

        try {
            this.configuration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reloads a custom configuration.
     */
    public void reload() {
        if (this.fileName.isEmpty()) return;

        if (!exists()) return;

        try {
            this.configuration = YamlConfiguration.loadConfiguration(this.file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}