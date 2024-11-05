package com.ryderbelserion.vital.api.files;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;

/**
 * Abstract base class for creating a custom file.
 *
 * <p>This class provides a framework for extending and implementing file operations
 * tailored to specific needs. Subclasses should define the actual behavior and properties
 * of the custom file type.
 *
 * @version 0.0.5
 * @since 0.0.5
 * @param <T> the type of the custom file that extends this class
 * @author ryderbelserion
 */
public abstract class CustomFile<T extends CustomFile<T>> {

    protected final Vital api = VitalProvider.get();
    protected final ComponentLogger logger = this.api.getLogger();
    protected final File dataFolder = this.api.getDataFolder();
    protected final boolean isVerbose = this.api.isVerbose();

    private final String effectiveName;
    private final File file;

    /**
     * Constructs a new {@link CustomFile} instance.
     *
     * <p>This constructor initializes the custom file by setting its effective name
     * (excluding the '.yml' extension) and the actual file object.
     *
     * @param file the file object to be wrapped by this custom file
     * @since 0.0.5
     */
    public CustomFile(final File file) {
        this.effectiveName = file.getName().replace(".yml", "");
        this.file = file;
    }

    /**
     * Returns the current instance of {@link CustomFile}.
     *
     * @return the current custom file instance
     * @since 0.0.5
     */
    public CustomFile<T> getInstance() {
        return this;
    }

    /**
     * Retrieves the effective name of the file, the name without the '.yml' extension.
     *
     * @return the effective name of the file
     * @since 0.0.5
     */
    public String getEffectiveName() {
        return this.effectiveName;
    }

    /**
     * Retrieves the name of the actual file.
     *
     * @return the name of the file
     * @since 0.0.5
     */
    public String getFileName() {
        return file.getName();
    }

    /**
     * Checks whether the file exists on the filesystem.
     *
     * @return {@code true} if the file exists, {@code false} otherwise
     * @since 0.0.5
     */
    public boolean isConfigurationLoaded() {
        return file.exists();
    }

    /**
     * Retrieves the file object wrapped by this custom file.
     *
     * @return the file object
     * @since 0.0.5
     */
    public File getFile() {
        return file;
    }
}