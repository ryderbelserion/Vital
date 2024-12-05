package com.ryderbelserion.vital.paper.api.files;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.files.enums.FileType;
import com.ryderbelserion.vital.utils.Methods;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages custom files for the application.
 *
 * <p>This class provides functionality to add, remove, and retrieve custom file instances,
 * supporting various file types such as YAML. It ensures that file operations are handled
 * efficiently and provides logging for important actions.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class PaperFileManager {

    private final Vital api = VitalProvider.get();
    private final ComponentLogger logger = this.api.getLogger();
    private final File dataFolder = this.api.getDataFolder();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, PaperCustomFile> files = new HashMap<>();

    private final Map<String, FileType> folders = new HashMap<>();

    /**
     * A file manager that handles yml configs.
     *
     * @since 0.1.0
     */
    public PaperFileManager() {}

    /**
     * Adds a folder to the manager and loads its contents.
     *
     * @param folder the folder name
     * @param fileType the type of files in the folder
     * @return the current instance of {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager addFolder(@NotNull final String folder, @NotNull final FileType fileType) {
        if (folder.isEmpty() || folder.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the folder as the folder is empty.");
            }

            return this;
        }

        if (!this.folders.containsKey(folder)) {
            this.folders.put(folder, fileType);
        }

        final File directory = new File(this.dataFolder, folder);

        if (!directory.exists()) {
            directory.mkdirs();

            Methods.extracts(PaperFileManager.class, String.format("/%s/", directory.getName()), directory.toPath(), false);
        }

        final File[] contents = directory.listFiles();

        if (contents == null) return this;

        final String extension = fileType.getExtension();

        for (final File file : contents) {
            if (file.isDirectory()) {
                final String[] files = file.list();

                if (files == null) continue;

                for (final String fileName : files) {
                    if (!fileName.endsWith("." + extension)) continue; // just in case people are weird

                    addFile(fileName, folder + File.separator + file.getName(), true, fileType);
                }

                continue;
            }

            final String fileName = file.getName();

            if (!fileName.endsWith("." + extension)) continue; // just in case people are weird

            addFile(fileName, folder, true, fileType);
        }

        return this;
    }

    /**
     * Adds a custom file to the cache.
     * 
     * @param customFile {@link PaperCustomFile}
     * @return the current instance of {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager addFile(@NotNull final PaperCustomFile customFile) {
        this.files.put(customFile.getEffectiveName(), customFile);
        
        return this;
    }

    /**
     * Adds a custom file with a default file type.
     *
     * <p>This method adds a file to the manager's map using the default file type of {@link FileType#NONE}.
     *
     * @param fileName the name of the file to add
     * @return the current instance of {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager addFile(@NotNull final String fileName) {
        return addFile(fileName, null, false, FileType.NONE);
    }

    /**
     * Adds a custom file to the manager's map.
     *
     * <p>This method supports adding YAML files and will throw an exception for unsupported file types.
     * The {@code isDynamic} parameter specifies whether the custom file is dynamic.
     *
     * @param fileName the name of the file to add
     * @param fileType the type of the file
     * @return the current instance of {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager addFile(@NotNull final String fileName, @NotNull final FileType fileType) {
        return addFile(fileName, null, false, fileType);
    }

    /**
     * Adds a custom file to the manager's map.
     *
     * <p>This method supports adding YAML files and will throw an exception for unsupported file types.
     * The {@code isDynamic} parameter specifies whether the custom file is dynamic.
     *
     * @param fileName the name of the file to add
     * @param folder the folder in which the file is located, or {@code null} if no folder is specified
     * @param isDynamic whether the custom file is dynamic
     * @param fileType the type of the file
     * @return the current instance of {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager addFile(@NotNull final String fileName, @Nullable final String folder, final boolean isDynamic, @NotNull final FileType fileType) {
        if (fileName.isEmpty() || fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot add the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = fileType.getExtension();

        final String strippedName = strip(fileName, extension);

        final File file = new File(this.dataFolder, folder != null ? folder + File.separator + fileName : fileName);

        if (!file.exists()) {
            if (this.isVerbose) {
                this.logger.warn("Successfully extracted file {} to {}", fileName, file.getPath());
            }

            this.api.saveResource(folder == null ? fileName : folder + File.separator + fileName, false, this.isVerbose);
        }

        switch (fileType) {
            case YAML -> {
                if (this.files.containsKey(strippedName)) {
                    this.files.get(strippedName).load();

                    return this;
                }

                this.files.put(strippedName, new PaperCustomFile(fileType, file, isDynamic).load());
            }

            case JSON -> throw new GenericException("The file type with extension " + extension + " is not currently supported.");

            case NONE -> {} // do nothing
        }

        return this;
    }

    /**
     * Saves a file with the specified name and type.
     *
     * @param fileName the name of the file, must not be null or empty
     * @return {@link PaperFileManager} the current instance of FileManager
     * @since 0.1.0
     */
    public PaperFileManager saveFile(@NotNull final String fileName) {
        if (fileName.isEmpty() || fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save the file as the file is null or empty.");
            }

            return this;
        }

        final String extension = FileType.YAML.getExtension();

        final String strippedName = strip(fileName, extension);

        if (!this.files.containsKey(strippedName)) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save the file as the file does not exist.");
            }

            return this;
        }

        this.files.get(strippedName).save();

        return this;
    }

    /**
     * Removes a custom file from the manager's map by its instance.
     *
     * <p>This method removes the file if it exists in the map.
     *
     * @param customFile the custom file instance to remove
     * @param purge whether to delete the physical file
     * @return the current instance of {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager removeFile(final PaperCustomFile customFile, final boolean purge) {
        return removeFile(customFile.getFileName(), customFile.getFileType(), purge);
    }

    /**
     * Removes a custom file from the manager's map by its name.
     *
     * <p>This method removes the file if it exists in the map.
     *
     * @param fileName the name of the file to remove
     * @param fileType the type of the file
     * @param purge whether to delete the physical file
     * @return the current instance of {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager removeFile(@NotNull final String fileName, @NotNull final FileType fileType, final boolean purge) {
        if (fileName.isEmpty() || fileName.isBlank()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot remove the file as the file is null or empty.");
            }

            return this;
        }

        final String strippedName = strip(fileName, fileType.getExtension());

        if (!this.files.containsKey(strippedName)) return this;

        final PaperCustomFile customFile = this.files.remove(fileName);

        if (purge) {
            final File file = customFile.getFile();

            if (file == null) return this;

            if (file.delete()) {
                if (this.isVerbose) {
                    this.logger.warn("Successfully deleted {}", fileName);
                }
            }

            return this;
        }

        customFile.save();

        return this;
    }

    /**
     * Reloads all files.
     *
     * @return {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager reloadFiles() {
        final List<String> forRemoval = new ArrayList<>();

        this.files.forEach((name, file) -> {
            if (file.getFile().exists()) {
                file.load();
            } else {
                forRemoval.add(name);
            }
        });

        forRemoval.forEach(this.files::remove);

        if (this.isVerbose && !forRemoval.isEmpty()) {
            this.logger.info("{} file(s) were removed from cache, because they did not exist.", forRemoval.size());
        }

        return this;
    }

    /**
     * Creates the data folder and anything else we need.
     *
     * @return {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager init() {
        this.dataFolder.mkdirs();

        this.folders.forEach(this::addFolder);

        return this;
    }

    /**
     * Purges all files.
     *
     * @return {@link PaperFileManager}
     * @since 0.1.0
     */
    public PaperFileManager purge() {
        this.files.clear();

        return this;
    }

    /**
     * Retrieves a custom file from the manager's map by its name.
     *
     * <p>This method returns the custom file instance if it exists in the map.
     *
     * @param fileName the name of the file to retrieve
     * @param fileType the type of the file
     * @return the custom file instance, or null if not found
     * @since 0.1.0
     */
    public @Nullable PaperCustomFile getFile(final String fileName, final FileType fileType) {
        return this.files.getOrDefault(strip(fileName, fileType.getExtension()), null);
    }

    /**
     * Removes the extension from the file name.
     *
     * @param fileName the file name to strip the extension from
     * @param extension the extension to remove
     * @return the file name without the extension
     * @since 0.1.0
     */
    public String strip(final String fileName, final String extension) {
        return fileName.replace("." + extension, "");
    }

    /**
     * Retrieves the map of custom files managed by this file manager.
     *
     * @return a map of custom files with their file names as keys
     * @since 0.1.0
     */
    public Map<String, PaperCustomFile> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }
}