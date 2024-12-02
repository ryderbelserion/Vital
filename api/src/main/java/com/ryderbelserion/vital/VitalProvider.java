package com.ryderbelserion.vital;

import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.UnavailableException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Provides access to the Vital API instance.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
@ApiStatus.Internal
public class VitalProvider {

    private static Vital vital = null;

    /**
     * Private constructor to prevent instantiation.
     *
     * @since 0.1.0
     */
    public VitalProvider() {
        throw new AssertionError();
    }

    /**
     * Gets the Vital API instance.
     *
     * @return the Vital instance
     * @since 0.1.0
     */
    @ApiStatus.Internal
    public static @NotNull Vital get() {
        Vital instance = VitalProvider.vital;

        if (instance == null) {
            throw new UnavailableException();
        }

        return instance;
    }

    /**
     * Registers the Vital API instance.
     *
     * @param vital the Vital instance to register
     * @since 0.1.0
     */
    @ApiStatus.Internal
    public static void register(final Vital vital) {
        VitalProvider.vital = vital;
    }

    /**
     * Unregisters the Vital API instance.
     *
     * @since 0.1.0
     */
    @ApiStatus.Internal
    public static void unregister() {
        VitalProvider.vital = null;
    }
}