package com.ryderbelserion.vital.discord.files;

import com.ryderbelserion.vital.core.util.FileUtil;
import com.ryderbelserion.vital.discord.VitalDiscord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.slf4j.Logger;
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

/**
 * A file manager that handles yml configs
 *
 * @author Ryder Belserion
 * @author BadBones69
 *
 * @version 1.8
 * @since 1.8
 */
public class FileManager {

    private final VitalDiscord vital;
    private final File dataFolder;
    private final Logger logger;

    /**
     * An empty constructor that does fuck all.
     */
    public FileManager(VitalDiscord vital) {
        this.vital = vital;
        this.dataFolder = this.vital.getDirectory();
        this.logger = this.vital.getLogger();
    }

    // Holds static files
    private final Map<String, YamlConfiguration> files = new HashMap<>();

    // Holds the folders to load dynamic files in
    private final Set<CustomFile> customFiles = new HashSet<>();
    private final Set<String> folders = new HashSet<>();

    /**
     * Creates the data folder and anything else we need.
     *
     * @since 1.8
     */
    public void init() {
        this.dataFolder.mkdirs();

        this.customFiles.clear();

        // Creates the custom folders.
        for (String folder : this.folders) {
            Path resolvedFolder = new File(this.dataFolder, folder).toPath();

            if (!Files.exists(resolvedFolder)) {
                // Create directory.
                try {
                    Files.createDirectory(resolvedFolder);
                } catch (IOException e) {
                    this.logger.error("Failed to create directory: {}...", resolvedFolder.toFile().getName());
                }

                // extract files if needed.
                FileUtil.extracts(FileManager.class, "/" + folder + "/", resolvedFolder, true);

                // get all files with recursion
                loadFiles(resolvedFolder.toFile(), folder);
            } else {
                loadFiles(resolvedFolder.toFile(), "");
            }
        }
    }

    /**
     * Reload a {@link YamlConfiguration}.
     *
     * @param file the name of the {@link YamlConfiguration} to save
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager reloadFile(@NotNull final String file) {
        if (file.isEmpty()) return this;

        final YamlConfiguration configuration = getFile(file);

        if (configuration == null) return this;

        final File key = new File(this.dataFolder, file);

        try {
            this.files.put(file, YamlConfiguration.loadConfiguration(key));
        } catch (Exception exception) {
            this.logger.error("Failed to load: {}...", file);
        }

        return this;
    }

    /**
     * Adds a {@link YamlConfiguration}.
     *
     * @param fileName the name of the {@link YamlConfiguration} to add
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager addFile(@NotNull final String fileName) {
        if (fileName.isEmpty()) return this;

        final File file = new File(this.dataFolder, fileName);

        try {
            if (!file.exists()) {
                FileUtil.extract(FileManager.class, fileName, this.dataFolder.toPath(), false);

                this.logger.info("Copied {} because it did not exist...", fileName);
            } else {
                this.logger.info("Loading the file {}...", fileName);
            }

            // Add other file
            this.files.put(fileName, YamlConfiguration.loadConfiguration(file));
        } catch (Exception exception) {
            this.logger.error("Failed to load or create {}...", fileName);
        }

        return this;
    }

    /**
     * Saves a {@link YamlConfiguration}.
     *
     * @param fileName the name of the {@link YamlConfiguration} to save
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager saveFile(@NotNull final String fileName) {
        if (fileName.isEmpty()) return this;

        YamlConfiguration configuration = getFile(fileName);

        if (configuration == null) return this;

        try {
            configuration.save(new File(this.dataFolder, fileName));
        } catch (Exception exception) {
            this.logger.error("Failed to save: {}...", fileName);
        }

        return this;
    }

    /**
     * Gets a {@link YamlConfiguration}.
     *
     * @param fileName the name of the {@link YamlConfiguration} to save
     * @return {@link YamlConfiguration}
     * @since 1.8
     */
    public @Nullable final YamlConfiguration getFile(@NotNull final String fileName) {
        if (fileName.isEmpty()) return null;

        YamlConfiguration configuration = null;

        for (String key : this.files.keySet()) {
            if (!key.equalsIgnoreCase(fileName)) continue;

            configuration = this.files.get(key);

            break;
        }

        return configuration;
    }

    /**
     * Reload all other {@link YamlConfiguration}'s.
     *
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager reloadFiles() {
        this.files.forEach((key, configuration) -> {
            try {
                configuration.save(key);
                configuration.load(key);
            } catch (IOException exception) {
                this.logger.error("Failed to load: {}...", key);
            }
        });

        return this;
    }

    /**
     * Load files with one level of recursion
     *
     * @param resolvedFolder the {@link Path} to check
     * @since 1.8
     */
    private void loadFiles(final File resolvedFolder, final String folder) {
        File[] filesList = resolvedFolder.listFiles();

        if (filesList != null) {
            for (File directory : filesList) {
                if (directory.isDirectory()) {
                    String[] dir = directory.list();

                    if (dir != null) {
                        for (String name : dir) {
                            if (!name.endsWith(".yml")) continue;

                            final CustomFile file = new CustomFile(this.logger, directory).apply(name);

                            if (file != null && file.exists()) {
                                this.customFiles.add(file);

                                this.logger.info("Loaded new custom file: {}/{}/{}.", folder, directory.getName(), name);
                            }
                        }
                    }
                } else {
                    String name = directory.getName();

                    if (!name.endsWith(".yml")) continue;

                    final CustomFile file = new CustomFile(this.logger, resolvedFolder).apply(name);

                    if (file != null && file.exists()) {
                        this.customFiles.add(file);

                        this.logger.info("Loaded new custom file: {}/{}.", folder, name);
                    }
                }
            }
        }
    }

    /**
     * Get a {@link CustomFile}.
     *
     * @param file the {@link CustomFile} without the file extension
     * @return {@link FileManager}
     * @since 1.8
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
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager addCustomFile(@Nullable final CustomFile file) {
        if (file == null) return this;

        this.customFiles.add(file);

        return this;
    }

    /**
     * Save a {@link CustomFile}.
     *
     * @param key the name of the {@link CustomFile} to save
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager saveCustomFile(@NotNull final String key) {
        if (key.isEmpty()) return this;

        final CustomFile file = getCustomFile(key);

        if (file == null) return this;

        file.save();

        return this;
    }

    /**
     * Reload a specific {@link CustomFile}.
     *
     * @param key the name of the {@link YamlConfiguration} to reload
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager reloadCustomFile(@NotNull final String key) {
        if (key.isEmpty()) return this;

        final CustomFile file = getCustomFile(key);

        if (file == null) return this;

        file.reload();

        return this;
    }

    /**
     * Adds a folder to the hashset if it doesn't exist.
     *
     * @param folder the folder to add
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager addFolder(@NotNull final String folder) {
        if (folder.isEmpty()) return this;
        if (this.folders.contains(folder)) return this;

        this.folders.add(folder);

        return this;
    }

    /**
     * Removes a folder from the hashset if it exists.
     *
     * @param folder the folder to remove
     * @return {@link FileManager}
     * @since 1.8
     */
    public @NotNull final FileManager removeFolder(@NotNull final String folder) {
        if (folder.isEmpty()) return this;

        this.folders.remove(folder);

        return this;
    }

    /**
     * Gets the root folder.
     *
     * @return the {@link Path}
     * @since 1.8
     */
    public final @NotNull File getDataFolder() {
        return this.dataFolder;
    }

    /**
     * Gets a set of folders.
     *
     * @return an unmodifiable set of folders
     * @since 1.8
     */
    public @NotNull final Set<String> getFolders() {
        return Collections.unmodifiableSet(this.folders);
    }

    /**
     * Gets a set of files.
     *
     * @return an unmodifiable set of files
     * @since 1.8
     */
    public @NotNull final Set<CustomFile> getCustomFiles() {
        return Collections.unmodifiableSet(this.customFiles);
    }

    /**
     * Gets a map of files.
     *
     * @return an unmodifiable map of other files
     * @since 1.8
     */
    public @NotNull final Map<String, YamlConfiguration> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }
}