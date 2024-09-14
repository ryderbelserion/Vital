package com.ryderbelserion.vital.paper.api.builders.gui.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Throws an exception.
 *
 * @author Matt
 * @version 0.0.1
 * @since 0.0.1
 */
public final class GuiException extends RuntimeException {

    /**
     * Throws an exception with a specific cause.
     *
     * @param message the message to send
     * @param cause the cause
     * @since 0.0.1
     */
    public GuiException(@NotNull final String message, @NotNull final Exception cause) {
        super(message, cause);
    }

    /**
     * Throws an exception with a default case.
     *
     * @param message the message to send
     * @since 0.0.1
     */
    public GuiException(@NotNull final String message) {
        super(message);
    }
}