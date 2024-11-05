package com.ryderbelserion.vital.api.files;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.common.api.Provider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import java.io.File;

/**
 * Creates a custom file.
 *
 * @author ryderbelserion
 * @version 1.0.0
 * @since 1.0.0
 */
public class CustomFile<T extends CustomFile<T>> {

    protected final VitalAPI api = Provider.getApi();
    protected final ComponentLogger logger = this.api.getComponentLogger();
    protected final boolean isVerbose = this.api.isVerbose();

    private final String effectiveName;
    private final File file;

    /**
     * A constructor to build a custom file.
     *
     * @param file {@link File}
     * @since 1.0.0
     */
    public CustomFile(final File file) {
        this.effectiveName = file.getName().replace(".yml", "");
        this.file = file;
    }

    /**
     * Get the custom file instance
     *
     * @return the custom file instance
     * @since 1.0.0
     */
    public CustomFile<T> getCustomFile() {
        return this;
    }

    /**
     * Gets the effective name i.e. without .yml.
     *
     * @return the effective name
     * @since 1.0.0
     */
    public String getEffectiveName() {
        return this.effectiveName;
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     * @since 1.0.0
     */
    public String getFileName() {
        return getFile().getName();
    }

    /**
     * Checks if a file exists.
     *
     * @return true or false
     * @since 1.0.0
     */
    public boolean exists() {
        return file.exists();
    }

    /**
     * Gets the file object.
     *
     * @return the file object
     * @since 1.0.0
     */
    public File getFile() {
        return this.file;
    }
}