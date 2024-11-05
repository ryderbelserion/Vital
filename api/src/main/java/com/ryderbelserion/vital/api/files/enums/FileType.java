package com.ryderbelserion.vital.api.files.enums;

/**
 * Enum representing different file types supported by the application.
 *
 * <p>This enum provides a way to define and manage different file types, such as YAML and JSON,
 * and includes functionality to retrieve a user-friendly name for each file type.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author ryderbelserion
 */
public enum FileType {

    YAML("yml"),
    JSON("json"),
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
     * @since 1.0.0
     */
    public String getExtension() {
        return this.extension;
    }
}