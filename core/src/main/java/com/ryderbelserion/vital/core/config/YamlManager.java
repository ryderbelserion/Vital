package com.ryderbelserion.vital.core.config;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.core.config.objects.CustomFile;
import com.ryderbelserion.vital.core.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A file manager that handles yml configs
 *
 * @author Ryder Belserion
 * @author BadBones69
 *
 * @version 1.5.3
 * @since 1.0
 */
public class YamlManager {

    /**
     * An empty constructor that does fuck all.
     */
    public YamlManager() {}

    private @NotNull final Vital api = Vital.api();
    private @NotNull final Path dataFolder = this.api.getDirectory().toPath();
    private @NotNull final Logger logger = this.api.getLogger();
    private final boolean isLogging = this.api.isLogging();

    // Holds static files
    private final Map<String, YamlFile> files = new HashMap<>();

    // Holds the folders to load dynamic files in
    private final Set<CustomFile> customFiles = new HashSet<>();
    private final Set<String> folders = new HashSet<>();

    /**
     * Creates the plugin directory.
     * @since 1.1
     */
    public void createPluginDirectory() {
        File directory = this.dataFolder.toFile();

        if (!directory.exists()) {
            if (directory.mkdir()) {
                if (this.isLogging) this.logger.warning("Created " + directory.getName() + " because it was not found.");
            }
        }
    }

    /**
     * Creates the data folder and anything else we need.
     * @since 1.0
     */
    public void init() {
        this.customFiles.clear();

        // Creates the custom folders.
        for (String folder : this.folders) {
            Path resolvedFolder = this.dataFolder.resolve(folder);

            if (!Files.exists(resolvedFolder)) {
                // Create directory.
                try {
                    Files.createDirectory(resolvedFolder);
                } catch (IOException e) {
                    this.logger.severe("Failed to create directory: " + resolvedFolder.toFile().getName() + "...");
                }

                // extract files if needed.
                FileUtil.extracts(YamlManager.class, "/" + folder + "/", resolvedFolder, true);

                // get all files with recursion
                loadFiles(resolvedFolder);
            } else {
                loadFiles(resolvedFolder);
            }
        }
    }

    /**
     * Reload a {@link YamlFile}.
     *
     * @param file the name of the {@link YamlFile} to save
     * @return {@link YamlManager}
     * @since 1.0
     */
    public @NotNull final YamlManager reloadFile(@NotNull final String file) {
        if (file.isEmpty()) return this;

        YamlFile yamlFile = getFile(file);

        if (yamlFile == null) return this;

        try {
            yamlFile.loadWithComments();
        } catch (Exception exception) {
            this.logger.log(Level.SEVERE, "Failed to load: " + file + "...", exception);
        }

        return this;
    }

    /**
     * Adds a {@link YamlFile}.
     *
     * @param fileName the name of the {@link YamlFile} to add
     * @return {@link YamlManager}
     * @since 1.0
     */
    public @NotNull final YamlManager addFile(@NotNull final String fileName) {
        if (fileName.isEmpty()) return this;

        YamlFile file = new YamlFile(this.dataFolder.resolve(fileName).toString());

        try {
            if (!file.exists()) {
                FileUtil.extract(YamlManager.class, fileName, this.dataFolder, false);

                if (this.isLogging) this.logger.info("Copied " + fileName + " because it did not exist...");
            } else {
                if (this.isLogging) this.logger.info("Loading the file " + fileName + "...");
            }

            // Load the file with comments intact
            file.loadWithComments();

            // Add other file
            this.files.put(fileName, file);
        } catch (Exception exception) {
            this.logger.log(Level.SEVERE, "Failed to load or create " + fileName + "...", exception);
        }

        return this;
    }

    /**
     * Saves a {@link YamlFile}.
     *
     * @param file the name of the {@link YamlFile} to save
     * @return {@link YamlManager}
     * @since 1.0
     */
    public @NotNull final YamlManager saveFile(@NotNull final String file) {
        if (file.isEmpty()) return this;

        YamlFile yamlFile = getFile(file);

        if (yamlFile == null) return this;

        try {
            yamlFile.save();
        } catch (Exception exception) {
            this.logger.log(Level.SEVERE, "Failed to save: " + file + "...", exception);
        }

        return this;
    }

    /**
     * Gets a {@link YamlFile}.
     *
     * @param file the name of the {@link YamlFile} to save
     * @return {@link YamlFile}
     * @since 1.0
     */
    public @Nullable final YamlFile getFile(@NotNull final String file) {
        if (file.isEmpty()) return null;

        YamlFile yamlFile = null;

        for (String key : this.files.keySet()) {
            if (!key.equalsIgnoreCase(file)) continue;

            yamlFile = this.files.get(key);

            break;
        }

        return yamlFile;
    }

    /**
     * Reload all other {@link YamlFile}'s.
     *
     * @return {@link YamlManager}
     * @since 1.0
     */
    public @NotNull final YamlManager reloadFiles() {
        this.files.forEach((key, yamlFile) -> {
            try {
                yamlFile.loadWithComments();
            } catch (IOException exception) {
                this.logger.log(Level.SEVERE, "Failed to load: " + key + "...", exception);
            }
        });

        return this;
    }

    /**
     * Load files with one level of recursion
     *
     * @param resolvedFolder the {@link Path} to check
     * @since 1.0
     */
    private void loadFiles(Path resolvedFolder) {
        FileUtil.getFiles(resolvedFolder, ".yml", false).forEach(fileName -> {
            try {
                CustomFile customFile = new CustomFile(resolvedFolder).apply(fileName);

                this.customFiles.add(customFile);
            } catch (Exception exception) {
                this.logger.log(Level.SEVERE, "Failed to create default file: " + resolvedFolder.toFile().getPath() + "...", exception);
            }
        });
    }

    /**
     * Get a {@link CustomFile}.
     *
     * @param file the {@link CustomFile} without the file extension
     * @return {@link YamlManager}
     * @since 1.0
     */
    public @Nullable final CustomFile getCustomFile(@NotNull final String file) {
        if (file.isEmpty()) return null;

        CustomFile customFile = null;

        for (CustomFile key : this.customFiles) {
            if (!key.getStrippedName().equalsIgnoreCase(file)) continue;

            customFile = key;

            break;
        }

        return customFile;
    }

    /**
     * Removes a {@link CustomFile} from the custom files map
     *
     * @param file the file to remove
     */
    public void removeCustomFile(@NotNull final String file) {
        @Nullable final CustomFile customFile = getCustomFile(file);

        // If null, return.
        if (customFile == null) return;

        // Remove if not null.
        this.customFiles.remove(customFile);
    }

    /**
     * Adds a {@link CustomFile}.
     *
     * @param file the {@link CustomFile} to add
     * @return {@link YamlManager}
     * @since 1.0
     */
    public final YamlManager addCustomFile(@Nullable final CustomFile file) {
        if (file == null) return this;

        this.customFiles.add(file);

        return this;
    }

    /**
     * Save a {@link CustomFile}.
     *
     * @param key the name of the {@link CustomFile} to save
     * @return {@link YamlManager}
     * @since 1.0
     */
    public final YamlManager saveCustomFile(@NotNull final String key) {
        if (key.isEmpty()) return this;

        CustomFile file = getCustomFile(key);

        if (file == null) return this;

        try {
            file.getYamlFile().save();
        } catch (IOException exception) {
            this.logger.log(Level.SEVERE, "Could not save " + key + "...", exception);
        }

        return this;
    }

    /**
     * Reload a specific {@link CustomFile}.
     *
     * @param key the name of the {@link YamlFile} to reload
     * @return {@link YamlManager}
     * @since 1.0
     */
    public final YamlManager reloadCustomFile(@NotNull final String key) {
        if (key.isEmpty()) return this;

        CustomFile file = getCustomFile(key);

        if (file == null) return this;

        try {
            file.getYamlFile().loadWithComments();
        } catch (IOException exception) {
            this.logger.log(Level.SEVERE, "Failed to load: " + key + "...", exception);
        }

        return this;
    }

    /**
     * Adds a folder to the hashset if it doesn't exist.
     *
     * @param folder the folder to add
     * @return {@link YamlManager}
     * @since 1.0
     */
    public final YamlManager addFolder(@NotNull final String folder) {
        if (folder.isEmpty()) return this;
        if (this.folders.contains(folder)) return this;

        this.folders.add(folder);

        return this;
    }

    /**
     * Removes a folder from the hashset if it exists.
     *
     * @param folder the folder to remove
     * @return {@link YamlManager}
     * @since 1.0
     */
    public final YamlManager removeFolder(@NotNull final String folder) {
        if (folder.isEmpty()) return this;

        this.folders.remove(folder);

        return this;
    }

    /**
     * Gets the root folder.
     *
     * @return the {@link Path}
     */
    public @NotNull final Path getDataFolder() {
        return this.dataFolder;
    }

    /**
     * Gets a set of folders.
     *
     * @return an unmodifiable set of folders
     * @since 1.0
     */
    public @NotNull final Set<String> getFolders() {
        return Collections.unmodifiableSet(this.folders);
    }

    /**
     * Gets a set of files.
     *
     * @return an unmodifiable set of files
     * @since 1.1
     */
    public Set<CustomFile> getCustomFiles() {
        return Collections.unmodifiableSet(this.customFiles);
    }

    /**
     * Gets a map of files.
     *
     * @return an unmodifiable map of other files
     * @since 1.1
     */
    public Map<String, YamlFile> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }
}