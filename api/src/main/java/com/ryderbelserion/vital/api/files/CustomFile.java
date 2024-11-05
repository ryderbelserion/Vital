package com.ryderbelserion.vital.api.files;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.files.enums.FileType;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;
import java.util.List;

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
     * Retrieves a string value from the configuration at the specified path, or an empty string if not found.
     *
     * @param path the configuration path to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the string value at the specified path, or an empty string if not found
     * @since 0.0.5
     */
    public abstract String getStringValue(final String path, final String defaultValue);

    /**
     * Retrieves a string value from the configuration at the specified path, or an empty string if not found.
     *
     * @param path the configuration path to retrieve the value from
     * @return the string value at the specified path, or an empty string if not found
     * @since 0.0.5
     */
    public abstract String getStringValue(final String path);

    /**
     * Retrieves a boolean value from the configuration at the specified path.
     *
     * @param path the configuration path to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the boolean value at the specified path, or the default value if not found
     * @since 0.0.5
     */
    public abstract boolean getBooleanValue(final String path, final boolean defaultValue);

    /**
     * Retrieves a boolean value from the configuration at the specified path, or {@code false} if not found.
     *
     * @param path the configuration path to retrieve the value from
     * @return the boolean value at the specified path, or {@code false} if not found
     * @since 0.0.5
     */
    public abstract boolean getBooleanValue(final String path);

    /**
     * Retrieves a double value from the configuration at the specified path.
     *
     * @param path the configuration path to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the double value at the specified path, or the default value if not found
     * @since 0.0.5
     */
    public abstract double getDoubleValue(final String path, final double defaultValue);

    /**
     * Retrieves a double value from the configuration at the specified path, or {@code Double.NaN} if not found.
     *
     * @param path the configuration path to retrieve the value from
     * @return the double value at the specified path, or {@code Double.NaN} if not found
     * @since 0.0.5
     */
    public abstract double getDoubleValue(final String path);

    /**
     * Retrieves a long value from the configuration at the specified path.
     *
     * @param path the configuration path to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the long value at the specified path, or the default value if not found
     * @since 0.0.5
     */
    public abstract long getLongValue(final String path, final long defaultValue);

    /**
     * Retrieves a long value from the configuration at the specified path, or {@code Long.MIN_VALUE} if not found.
     *
     * @param path the configuration path to retrieve the value from
     * @return the long value at the specified path, or {@code Long.MIN_VALUE} if not found
     * @since 0.0.5
     */
    public abstract long getLongValue(final String path);

    /**
     * Retrieves an integer value from the configuration at the specified path.
     *
     * @param path the configuration path to retrieve the value from
     * @param defaultValue the default value to return if the path does not exist
     * @return the integer value at the specified path, or the default value if not found
     * @since 0.0.5
     */
    public abstract int getIntValue(final String path, final int defaultValue);

    /**
     * Retrieves an integer value from the configuration at the specified path, or {@code Integer.MIN_VALUE} if not found.
     *
     * @param path the configuration path to retrieve the value from
     * @return the integer value at the specified path, or {@code Integer.MIN_VALUE} if not found
     * @since 0.0.5
     */
    public abstract int getIntValue(final String path);

    /**
     * Retrieves a list of strings from the configuration at the specified path.
     *
     * @param path the configuration path to retrieve the list from
     * @return a list of strings at the specified path
     * @since 0.0.5
     */
    public abstract List<String> getStringList(final String path);

    /**
     * Loads the configuration for the custom file.
     *
     * @return the current instance of the custom file
     * @since 0.0.5
     */
    public abstract CustomFile<T> loadConfiguration();

    /**
     * Saves the configuration for the custom file.
     *
     * @return the current instance of the custom file
     * @since 0.0.5
     */
    public abstract CustomFile<T> saveConfiguration();

    /**
     * Retrieves the file type of this custom file.
     *
     * @return the file type
     * @since 0.0.5
     */
    public abstract FileType getFileType();

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
        return this.file.getName();
    }

    /**
     * Checks whether the file exists on the filesystem.
     *
     * @return {@code true} if the file exists, {@code false} otherwise
     * @since 0.0.5
     */
    public boolean isConfigurationLoaded() {
        return this.file.exists();
    }

    /**
     * Retrieves the file object wrapped by this custom file.
     *
     * @return the file object
     * @since 0.0.5
     */
    public File getFile() {
        return this.file;
    }
}