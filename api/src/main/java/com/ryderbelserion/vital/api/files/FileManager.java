package com.ryderbelserion.vital.api.files;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.UnavailableException;
import com.ryderbelserion.vital.api.files.enums.FileType;
import com.ryderbelserion.vital.api.files.types.YamlCustomFile;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages custom files for the application.
 *
 * <p>This class provides functionality to add, remove, and retrieve custom file instances,
 * supporting various file types such as YAML. It ensures that file operations are handled
 * efficiently and provides logging for important actions.
 *
 * @version 0.0.5
 * @since 0.0.5
 */
public class FileManager {

    private final Vital api = VitalProvider.get();
    private final ComponentLogger logger = this.api.getLogger();
    private final File dataFolder = this.api.getDataFolder();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, CustomFile<? extends CustomFile<?>>> files = new HashMap<>();

    /**
     * Default constructor.
     * 
     * @since 0.0.5
     */
    public FileManager() {}
    
    /**
     * Adds a custom file to the manager's map.
     *
     * <p>This method supports adding YAML files and will throw an exception for unsupported file types.
     *
     * @param fileName the name of the file to add
     * @param fileType the type of the file
     * @since 0.0.5
     */
    public void addFile(final String fileName, final FileType fileType) {
        switch (fileType) {
            case YAML -> this.files.put(fileName, new YamlCustomFile(new File(this.dataFolder, fileName)).loadConfiguration());

            case JSON -> throw new UnavailableException("The file type " + fileType.getPrettyName() + " is not currently supported.");
        }
    }

    /**
     * Removes a custom file from the manager's map by its instance.
     *
     * <p>This method removes the file if it exists in the map.
     *
     * @param customFile the custom file instance to remove
     * @since 0.0.5
     */
    public void removeFile(final CustomFile<? extends CustomFile<?>> customFile) {
        final String fileName = customFile.getFileName();

        if (!this.files.containsKey(fileName)) return;

        removeFile(fileName);
    }

    /**
     * Removes a custom file from the manager's map by its name.
     *
     * <p>This method removes the file if it exists in the map.
     *
     * @param fileName the name of the file to remove
     * @since 0.0.5
     */
    public void removeFile(final String fileName) {
        this.files.remove(fileName);
    }

    /**
     * Retrieves a custom file from the manager's map by its name.
     *
     * <p>This method returns the custom file instance if it exists in the map.
     *
     * @param fileName the name of the file to retrieve
     * @return the custom file instance, or null if not found
     * @since 0.0.5
     */
    public CustomFile<? extends CustomFile<?>> getFile(final String fileName) {
        return this.files.get(fileName);
    }
}