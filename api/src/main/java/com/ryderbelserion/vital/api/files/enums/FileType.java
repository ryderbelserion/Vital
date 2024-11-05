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

    YAML("yaml"),
    JSON("json");

    private final String prettyName;

    /**
     * Constructs a {@link FileType} with a user-friendly name.
     *
     * @param prettyName the user-friendly name of the file type
     * @since 1.0.0
     */
    FileType(final String prettyName) {
        this.prettyName = prettyName;
    }

    /**
     * Retrieves the user-friendly name of the file type.
     *
     * @return the user-friendly name of the file type
     * @since 1.0.0
     */
    public String getPrettyName() {
        return this.prettyName;
    }
}