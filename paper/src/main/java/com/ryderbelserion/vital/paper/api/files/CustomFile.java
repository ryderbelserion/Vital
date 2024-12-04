package com.ryderbelserion.vital.paper.api.files;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.files.enums.FileType;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public class CustomFile {

    /**
     * Vital Provider
     */
    protected final Vital api = VitalProvider.get();
    /**
     * Component logger
     */
    protected final ComponentLogger logger = this.api.getLogger();
    /**
     * Should we log?
     */
    protected final boolean isVerbose = this.api.isVerbose();

    private final String effectiveName;
    private final FileType fileType;
    private final boolean isDynamic;
    private final File file;

    /**
     * Constructs a new {@link CustomFile} instance.
     *
     * <p>This constructor initializes the custom file by setting its effective name
     * (excluding the '.yml' extension) and the actual file object.
     *
     * @param file the file object to be wrapped by this custom file
     * @param isDynamic whether the custom file should be treated as dynamic
     * @since 0.1.0
     */
    public CustomFile(final FileType fileType, final File file, final boolean isDynamic) {
        this.effectiveName = file.getName().replace(".yml", "");
        this.isDynamic = isDynamic;
        this.fileType = fileType;
        this.file = file;
    }

    private YamlConfiguration configuration;

    /**
     * Loads from disk
     *
     * @return {@link CustomFile}
     * @since 0.1.0
     */
    public final CustomFile load() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        try {
            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.file)).join();
        } catch (Exception exception) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load configuration file: {}", getFileName(), exception);
            }
        }

        return this;
    }

    /**
     * Saves to disk
     *
     * @return {@link CustomFile}
     * @since 0.1.0
     */
    public final CustomFile save() {
        if (getFile().isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save configuration, as {} is a directory.", getFileName());
            }

            return this;
        }

        if (this.configuration == null) {
            if (this.isVerbose) {
                this.logger.error("Configuration is null, cannot save {}!", getFileName());
            }

            return this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.configuration.save(this.file);
            } catch (Exception exception) {
                if (this.isVerbose) this.logger.warn("Cannot save configuration file: {}", getFileName(), exception);
            }
        });

        return this;
    }

    /**
     * Gets the {@link YamlConfiguration}
     *
     * @return {@link YamlConfiguration}
     * @since 0.1.0
     */
    public final @Nullable YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Checks if {@link YamlConfiguration} is null
     *
     * @return true or false
     */
    public final boolean isConfigurationLoaded() {
        return getConfiguration() != null;
    }

    /**
     * Checks if the custom file is dynamic.
     *
     * @return {@code true} if the custom file is dynamic, {@code false} otherwise
     * @since 0.1.0
     */
    public boolean isDynamic() {
        return this.isDynamic;
    }

    /**
     * Returns the current instance of {@link CustomFile}.
     *
     * @return the current custom file instance
     * @since 0.1.0
     */
    public CustomFile getInstance() {
        return this;
    }

    /**
     * Retrieves the effective name of the file, the name without the '.yml' extension.
     *
     * @return the effective name of the file
     * @since 0.1.0
     */
    public String getEffectiveName() {
        return this.effectiveName;
    }

    /**
     * Retrieves the name of the actual file.
     *
     * @return the name of the file
     * @since 0.1.0
     */
    public String getFileName() {
        return this.file.getName();
    }

    /**
     * Checks whether the file exists on the filesystem.
     *
     * @return {@code true} if the file exists, {@code false} otherwise
     * @since 0.1.0
     */
    public boolean isFileLoaded() {
        return this.file.exists();
    }

    /**
     * Retrieves the file type of this custom file.
     *
     * @return the file type
     * @since 0.1.0
     */
    public FileType getFileType() {
        return this.fileType;
    }

    /**
     * Retrieves the file object wrapped by this custom file.
     *
     * @return the file object
     * @since 0.1.0
     */
    public File getFile() {
        return this.file;
    }
}