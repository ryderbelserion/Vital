package com.ryderbelserion.vital.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * Throws an exception.
 * <p>
 * This class represents a generic illegal state exception with an optional cause.
 * </p>
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public final class GenericException extends IllegalStateException {

    /**
     * Throws an exception with a specific cause.
     *
     * @param message the message to send
     * @param cause the cause
     * @since 0.1.0
     */
    public GenericException(@NotNull final String message, @NotNull final Exception cause) {
        super(message, cause);
    }

    /**
     * Throws an exception with a default case.
     *
     * @param message the message to send
     * @since 0.1.0
     */
    public GenericException(@NotNull final String message) {
        super(message);
    }
}