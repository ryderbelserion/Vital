package com.ryderbelserion.vital.paper.api.catches;

import org.jetbrains.annotations.NotNull;

/**
 * Throws an exception.
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public final class GenericException extends RuntimeException {

    /**
     * Throws an exception with a specific cause.
     *
     * @param message the message to send
     * @param cause the cause
     * @since 0.0.1
     */
    public GenericException(@NotNull final String message, @NotNull final Exception cause) {
        super(message, cause);
    }

    /**
     * Throws an exception with a default case.
     *
     * @param message the message to send
     * @since 0.0.1
     */
    public GenericException(@NotNull final String message) {
        super(message);
    }
}