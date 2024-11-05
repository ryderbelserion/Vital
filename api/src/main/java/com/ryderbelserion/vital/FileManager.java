package com.ryderbelserion.vital.common.api.managers;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.common.api.Provider;
import com.ryderbelserion.vital.common.api.managers.files.CustomFile;
import com.ryderbelserion.vital.common.api.managers.files.enums.FileType;
import com.ryderbelserion.vital.common.api.managers.files.types.YamlCustomFile;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileManager {

    private final VitalAPI api = Provider.getApi();
    private final ComponentLogger logger = this.api.getComponentLogger();
    private final File dataFolder = this.api.getDirectory();
    private final boolean isVerbose = this.api.isVerbose();

    private final Map<String, CustomFile<? extends CustomFile<?>>> files = new HashMap<>();

    public void addFile(final String fileName, final FileType file) {
        switch (file) {
            case YAML -> this.files.put(fileName, new YamlCustomFile(new File(this.dataFolder, fileName)).load());

            case JSON -> {

            }
        }
    }

    public void removeFile(final String fileName) {
        this.files.remove(fileName);
    }

    public CustomFile<? extends CustomFile<?>> getFile(final String fileName) {
        return this.files.get(fileName);
    }
}