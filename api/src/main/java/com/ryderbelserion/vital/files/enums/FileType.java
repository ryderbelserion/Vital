package com.ryderbelserion.vital.files.enums;

/**
 * Enum representing different file types supported by the application.
 *
 * <p>This enum provides a way to define and manage different file types, such as YAML and JSON,
 * and includes functionality to retrieve a user-friendly name for each file type.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public enum FileType {

    /**
     * YAML file format, typically used for configuration files.
     */
    YAML("yml"),

    /**
     * JSON file format, commonly used for data interchange.
     */
    JSON("json"),

    /**
     * No specific file format, used as a default or placeholder.
     */
    NONE("none");

    private final String extension;

    /**
     * Constructs a {@link FileType} with an extension.
     *
     * @param extension the extension of the file type
     * @since 1.0.0
     */
    FileType(final String extension) {
        this.extension = extension;
    }

    /**
     * Retrieves the extension of the file type.
     *
     * @return the extension of the file type
     * @since 0.6.0
     */
    public String getExtension() {
        return this.extension;
    }
}