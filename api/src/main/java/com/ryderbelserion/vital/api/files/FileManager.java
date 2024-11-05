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

public class FileManager {

    private final Vital api = VitalProvider.get();
    private final ComponentLogger logger = this.api.getLogger();
    private final File dataFolder = this.api.getDataFolder();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, CustomFile<? extends CustomFile<?>>> files = new HashMap<>();

    /**
     * Adds a file to the map.
     *
     * @param fileName the file name
     * @param file the file type
     */
    public void addFile(final String fileName, final FileType file) {
        switch (file) {
            case YAML -> this.files.put(fileName, new YamlCustomFile(new File(this.dataFolder, fileName)).load());

            case JSON -> throw new UnavailableException("The file type " + file.getPrettyName() + " is not currently supported.");
        }
    }

    /**
     * Removes a file from the map by custom file.
     *
     * @param file {@link CustomFile}
     */
    public void removeFile(final CustomFile<? extends CustomFile<?>> file) {
        final String fileName = file.getFileName();

        if (!this.files.containsKey(fileName)) return;

        removeFile(fileName);
    }

    /**
     * Removes a file from the map by file name.
     *
     * @param fileName the file name
     */
    public void removeFile(final String fileName) {
        this.files.remove(fileName);
    }

    /**
     * Fetches a file from the map by file name.
     *
     * @param fileName the file name
     * @return {@link CustomFile}
     */
    public CustomFile<? extends CustomFile<?>> getFile(final String fileName) {
        return this.files.get(fileName);
    }
}