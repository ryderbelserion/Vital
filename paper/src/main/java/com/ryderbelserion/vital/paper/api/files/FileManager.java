package com.ryderbelserion.vital.paper.api.files;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.common.api.Provider;
import com.ryderbelserion.vital.common.utils.FileUtil;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A file manager that handles yml configs.
 *
 * @author Ryder Belserion
 *
 * @version 0.0.7
 * @since 0.0.1
 */
@SuppressWarnings("LoggingSimilarMessage")
public class FileManager {

    /**
     * A file manager that handles yml configs.
     *
     * @since 0.0.1
     */
    public FileManager() {}

    private final VitalAPI api = Provider.getApi();
    private final ComponentLogger logger = this.api.getComponentLogger();
    private final File dataFolder = this.api.getDirectory();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, CustomFile> files = new HashMap<>();

    private final Map<String, CustomFile> customFiles = new HashMap<>();
    private final Set<String> folders = new HashSet<>();

    /**
     * Creates the data folder and anything else we need.
     *
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager init() {
        this.dataFolder.mkdirs();

        for (final String key : this.folders) {
            File folder = new File(this.dataFolder, key);

            if (!folder.exists()) {
                folder.mkdir();

                FileUtil.extracts(FileManager.class, String.format("/%s/", folder.getName()), folder.toPath(), true);
            }

            File[] contents = folder.listFiles();

            if (contents == null) return this;

            for (final File file : contents) {
                if (file.isDirectory()) {
                    final String[] files = file.list();

                    if (files == null) continue;

                    for (String fileName : files) {
                        if (!fileName.endsWith(".yml")) continue;

                        addFile(true, new File(file, fileName));
                    }
                } else {
                    if (!file.getName().endsWith(".yml")) continue;

                    addFile(true, file);
                }
            }
        }

        return this;
    }

    /**
     * Reloads all files.
     *
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager reloadFiles() {
        this.customFiles.forEach((key, file) -> file.load());
        
        this.files.forEach((key, file) -> file.load());

        return this;
    }

    /**
     * Purge all data.
     *
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager purge() {
        this.files.clear();
        this.customFiles.clear();
        this.folders.clear();
        
        return this;
    }


    /**
     * Runs any code.
     *
     * @param consumer {@link Consumer}
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager run(final Consumer<FileManager> consumer) {
        consumer.accept(this);
        
        return this;
    }

    /**
     * Adds a file to the cache.
     *
     * @param isDynamic true or false
     * @param file {@link File}
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager addFile(final boolean isDynamic, final File file) {
        if (file == null) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the file as the file is null.");
            }

            return this;
        }

        final String fileName = file.getName();
        final String cleanName = strip(fileName, "yml");

        if (isDynamic) {
            if (!fileName.endsWith(".yml")) {
                if (this.isVerbose) {
                    this.logger.warn("Cannot add {}, because it does not end in .yml", fileName);
                }

                return this;
            }
            
            if (this.customFiles.containsKey(cleanName)) {
                if (this.isVerbose) {
                    this.logger.warn("Cannot add {}, because it already exists. We are reloading the config!", fileName);
                }

                this.customFiles.get(cleanName).load();

                return this;
            }

            if (this.isVerbose) {
                this.logger.warn("Successfully loaded file {} in {}", fileName, file.getPath());
            }

            this.customFiles.put(cleanName, new CustomFile(fileName, file).load());

            return this;
        }

        if (!fileName.endsWith(".yml")) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add {}, because it does not end in .yml", fileName);
            }

            return this;
        }

        if (!file.exists()) {
            this.api.saveResource(fileName, false);
        }

        if (this.files.containsKey(cleanName)) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add {}, because it already exists. We are reloading the config!", fileName);
            }

            this.files.get(cleanName).load();

            return this;
        }

        this.files.put(cleanName, new CustomFile(fileName, file).load());

        if (this.isVerbose) {
            this.logger.warn("Successfully loaded file {} in {}", fileName, file.getPath());
        }

        return this;
    }

    /**
     * Adds a file to the cache.
     *
     * @param isDynamic true or false
     * @param file {@link CustomFile}
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager addFile(final boolean isDynamic, final CustomFile file) {
        final String cleanName = file.getCleanName();

        if (isDynamic) {
            this.customFiles.put(cleanName, file.load());
        } else {
            this.files.put(cleanName, file.load());
        }

        return this;
    }

    /**
     * Adds a file to the cache.
     *
     * @param file {@link CustomFile}
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager addFile(final CustomFile file) {
        return addFile(false, file);
    }

    /**
     * Adds a file to the cache.
     *
     * @param file {@link File}
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager addFile(final File file) {
        return addFile(false, file);
    }

    /**
     * Removes a file from the cache.
     *
     * @param fileName the name of the file
     * @param isDynamic true or false
     * @param purge true or false
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager removeFile(final boolean isDynamic, final String fileName, final boolean purge) {
        if (fileName.isEmpty()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot remove the file as the name is empty.");
            }

            return this;
        }

        final String cleanName = strip(fileName, "yml");

        final CustomFile customFile = isDynamic ? this.customFiles.get(cleanName) : this.files.get(cleanName);

        if (customFile == null) return this;

        if (isDynamic) {
            this.customFiles.remove(cleanName);
        } else {
            this.files.remove(cleanName);
        }

        if (purge) {
            final File file = customFile.getFile();

            if (file == null) return this;

            file.delete();

            if (this.isVerbose) {
                this.logger.warn("Successfully deleted {}", fileName);
            }
        } else {
            customFile.save();
        }

        return this;
    }

    /**
     * Removes a file from the cache.
     *
     * @param fileName the name of the file
     * @param purge true or false
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager removeFile(final String fileName, final boolean purge) {
        return removeFile(false, fileName, purge);
    }

    /**
     * Removes a file from the cache.
     *
     * @param fileName the name of the file
     * @param isDynamic true or false
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager removeFile(final boolean isDynamic, final String fileName) {
        return removeFile(isDynamic, fileName, false);
    }

    /**
     * Removes a file from the cache.
     *
     * @param fileName the name of the file
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager removeFile(final String fileName) {
        return removeFile(fileName, false);
    }

    /**
     * Removes a file from the cache.
     *
     * @param fileName the name of the file
     * @param isDynamic true or false
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager saveFile(final boolean isDynamic, final String fileName) {
        final String cleanName = strip(fileName, "yml");

        if (isDynamic) {
            this.customFiles.get(cleanName).save();
        } else {
            this.files.get(cleanName).save();
        }

        return this;
    }

    /**
     * Removes a file from the cache.
     *
     * @param fileName the name of the file
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager saveFile(final String fileName) {
        return saveFile(false, fileName);
    }

    /**
     * Adds a folder to the hashset.
     *
     * @param folder the folder to add
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager addFolder(final String folder) {
        if (folder.isEmpty() || this.folders.contains(folder)) return this;

        this.folders.add(folder);

        return this;
    }

    /**
     * Removes a folder from the hashset.
     *
     * @param folder the folder to remove
     * @return {@link FileManager}
     * @since 0.0.1
     */
    public final FileManager removeFolder(final String folder) {
        if (folder.isEmpty()) return this;

        this.folders.remove(folder);

        return this;
    }

    /**
     * Gets a file from the cache.
     *
     * @param fileName the name of the file
     * @param isCustom true or false
     * @return {@link CustomFile}
     * @since 0.0.1
     */
    public final CustomFile getFile(final String fileName, final boolean isCustom) {
        final String cleanName = strip(fileName, "yml");

        return isCustom ? this.customFiles.get(cleanName) : this.files.get(cleanName);
    }

    /**
     * Gets a file from the cache.
     *
     * @param fileName the name of the file
     * @return {@link CustomFile}
     * @since 0.0.1
     */
    public final CustomFile getFile(final String fileName) {
        return getFile(fileName, false);
    }

    /**
     * Gets a set of folders.
     *
     * @return the set of folders
     * @since 0.0.1
     */
    public final Set<String> getFolders() {
        return Collections.unmodifiableSet(this.folders);
    }

    /**
     * Get a map of custom files.
     *
     * @return the map of custom files
     * @since 0.0.1
     */
    public final Map<String, CustomFile> getCustomFiles() {
        return Collections.unmodifiableMap(this.customFiles);
    }

    /**
     * Get a map of custom files.
     *
     * @return the map of custom files
     * @since 0.0.1
     */
    public final Map<String, CustomFile> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }

    /**
     * Removes a prefix from the file name.
     *
     * @param fileName the file name
     * @param prefix the prefix to remove
     * @return the file name without the prefix
     * @since 0.0.1
     */
    public final String strip(final String fileName, final String prefix) {
        return fileName.replace("." + prefix, "");
    }
}