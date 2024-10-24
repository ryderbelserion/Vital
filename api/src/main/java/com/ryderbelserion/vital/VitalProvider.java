package com.ryderbelserion.vital;

import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.UnavailableException;
import org.jetbrains.annotations.NotNull;

/**
 * Vital api's provider
 *
 * @author ryderbelserion
 * @version 2.0.0
 * @since 2.0.0
 */
public class VitalProvider {

    private static Vital vital = null;

    /**
     * Vital api's provider
     *
     * @since 2.0.1
     */
    public VitalProvider() {
        throw new AssertionError();
    }

    /**
     * Gets the vital api
     *
     * @author ryderbelserion
     * @return {@link Vital}
     * @since 2.0.0
     */
    public static @NotNull Vital get() {
        Vital instance = VitalProvider.vital;

        if (instance == null) {
            throw new UnavailableException();
        }

        return instance;
    }

    /**
     * Registers vital api.
     *
     * @param vital api instance
     * @since 2.0.0
     */
    public static void register(final Vital vital) {
        VitalProvider.vital = vital;
    }

    /**
     * Unregisters vital api.
     *
     * @since 2.0.0
     */
    public static void unregister() {
        VitalProvider.vital = null;
    }
}