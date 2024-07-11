package com.ryderbelserion.vital.paper.files.config;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.core.util.FileUtil;
import com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A file manager that handles yml configs.
 *
 * @author Ryder Belserion
 * @author BadBones69
 *
 * @version 1.8.4
 * @since 1.0
 */
public class FileManager {

    private final JavaPlugin plugin;

    public FileManager(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private @NotNull final Vital api = Vital.api();
    private @NotNull final File dataFolder = this.api.getDirectory();
    private @NotNull final ComponentLogger logger = this.api.getLogger();

    private final boolean isLogging = this.api.isLogging();

    private final Map<String, YamlConfiguration> files = new HashMap<>();

    private final Set<CustomFile> customFiles = new HashSet<>();
    private final Set<String> folders = new HashSet<>();

    /**
     * Creates the data folder and anything else we need.
     *
     * @since 1.0
     */
    public void init() {
        this.dataFolder.mkdirs();

        this.customFiles.clear();

        for (String key : this.folders) {
            File folder = new File(this.dataFolder, key);

            if (!folder.exists()) {
                folder.mkdir();

                FileUtil.extracts(FileManager.class, String.format("/%s/", folder.getName()), folder.toPath(), true);
            }

            File[] files = folder.listFiles();

            if (files == null) return;

            for (File pair : files) {
                if (pair.isDirectory()) {
                    String[] directory = pair.list();

                    if (directory == null || directory.length == 0) continue;

                    for (String fileName : directory) {
                        if (!fileName.endsWith(".yml")) return;

                        final CustomFile file = new CustomFile(this.plugin, folder).apply(fileName);

                        if (file != null && file.exists()) {
                            this.customFiles.add(file);
                        }
                    }

                    continue;
                }

                if (!pair.getName().endsWith(".yml")) return;

                final CustomFile file = new CustomFile(this.plugin, folder).apply(pair.getName());

                if (file != null && file.exists()) {
                    this.customFiles.add(file);
                }
            }
        }
    }

    /**
     * Reload a {@link YamlConfiguration}.
     *
     * @param file the name of the {@link YamlConfiguration} to save
     * @return {@link FileManager}
     * @since 1.0
     */
    public @NotNull final FileManager reloadFile(@NotNull final String file) {
        if (file.isEmpty()) return this;

        @Nullable final YamlConfiguration configuration = getFile(file);

        if (configuration == null) return this;

        final File key = new File(this.dataFolder, file);

        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                try {
                    files.put(file, YamlConfiguration.loadConfiguration(key));
                } catch (Exception exception) {
                    if (isLogging) logger.error("Failed to load: {}...", file, exception);
                }
            }
        }.run(this.plugin);

        return this;
    }

    /**
     * Adds a {@link YamlConfiguration}.
     *
     * @param fileName the name of the {@link YamlConfiguration} to add
     * @return {@link FileManager}
     * @since 1.0
     */
    public @NotNull final FileManager addFile(@NotNull final String fileName) {
        if (fileName.isEmpty()) return this;

        final File file = new File(this.dataFolder, fileName);

        try {
            if (!file.exists()) {
                FileUtil.extract(FileManager.class, fileName, this.dataFolder.toPath(), false);

                if (this.isLogging) this.logger.info("Copied {} because it did not exist...", fileName);
            } else {
                if (this.isLogging) this.logger.info("Loading the file {}...", fileName);
            }

            new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
                @Override
                public void run() {
                    files.put(fileName, YamlConfiguration.loadConfiguration(file));
                }
            }.run(this.plugin);
        } catch (Exception exception) {
            if (this.isLogging) this.logger.error("Failed to load or create {}...", fileName, exception);
        }

        return this;
    }

    /**
     * Saves a {@link YamlConfiguration}.
     *
     * @param fileName the name of the {@link YamlConfiguration} to save
     * @return {@link FileManager}
     * @since 1.0
     */
    public @NotNull final FileManager saveFile(@NotNull final String fileName) {
        if (fileName.isEmpty()) return this;

        @Nullable final YamlConfiguration configuration = getFile(fileName);

        if (configuration == null) return this;

        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                try {
                    configuration.save(new File(dataFolder, fileName));
                } catch (Exception exception) {
                    if (isLogging) logger.error("Failed to save: {}...", fileName, exception);
                }
            }
        }.run(this.plugin);

        return this;
    }

    /**
     * Gets a {@link YamlConfiguration}.
     *
     * @param fileName the name of the {@link YamlConfiguration} to save
     * @return {@link YamlConfiguration}
     * @since 1.0
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
     * @since 1.0
     */
    public @NotNull final FileManager reloadFiles() {
        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                files.forEach((key, configuration) -> {
                    try {
                        // Only load the configuration which takes disk changes and loads them into memory.
                        configuration.load(key);
                    } catch (IOException | InvalidConfigurationException exception) {
                        if (isLogging) logger.error("Failed to load: {}...", key, exception);
                    }
                });
            }
        }.run(this.plugin);

        return this;
    }

    /**
     * Get a {@link CustomFile}.
     *
     * @param file the {@link CustomFile} without the file extension
     * @return {@link FileManager}
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
     * Removes a {@link CustomFile} from the custom files map.
     *
     * @param file the file to remove
     */
    public void removeCustomFile(@NotNull final String file) {
        if (file.isEmpty()) return;

        @Nullable final CustomFile customFile = getCustomFile(file);

        if (customFile == null) return;

        this.customFiles.remove(customFile);
    }

    /**
     * Adds a {@link CustomFile}.
     *
     * @param file the {@link CustomFile} to add
     * @return {@link FileManager}
     * @since 1.0
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
     * @since 1.0
     */
    public @NotNull final FileManager saveCustomFile(@NotNull final String key) {
        if (key.isEmpty()) return this;

        @Nullable final CustomFile file = getCustomFile(key);

        if (file == null) return this;

        file.save();

        return this;
    }

    /**
     * Reload a specific {@link CustomFile}.
     *
     * @param key the name of the {@link YamlConfiguration} to reload
     * @return {@link FileManager}
     * @since 1.0
     */
    public @NotNull final FileManager reloadCustomFile(@NotNull final String key) {
        if (key.isEmpty()) return this;

        @Nullable final CustomFile file = getCustomFile(key);

        if (file == null) return this;

        file.reload();

        return this;
    }

    /**
     * Adds a folder to the hashset if it doesn't exist.
     *
     * @param folder the folder to add
     * @return {@link FileManager}
     * @since 1.0
     */
    public @NotNull final FileManager addFolder(@NotNull final String folder) {
        if (folder.isEmpty() || this.folders.contains(folder)) return this;

        this.folders.add(folder);

        return this;
    }

    /**
     * Removes a folder from the hashset if it exists.
     *
     * @param folder the folder to remove
     * @return {@link FileManager}
     * @since 1.0
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
     */
    public @NotNull final File getDataFolder() {
        return this.dataFolder;
    }

    /**
     * Gets a map of files.
     *
     * @return an unmodifiable map of other files
     * @since 1.1
     */
    public @NotNull final Map<String, YamlConfiguration> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }

    /**
     * Gets a set of files.
     *
     * @return an unmodifiable set of files
     * @since 1.1
     */
    public @NotNull final Set<CustomFile> getCustomFiles() {
        return Collections.unmodifiableSet(this.customFiles);
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
}