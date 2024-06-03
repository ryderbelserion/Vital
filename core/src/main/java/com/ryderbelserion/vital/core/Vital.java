package com.ryderbelserion.vital.core;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Logger;

/**
 * An abstract class to extend in each platform.
 *
 * @author Ryder Belserion
 * @version 1.5
 * @since 1.0
 */
public abstract class Vital {

    /**
     * Gets the instance of {@link Vital}.
     *
     * @return {@link Vital}
     * @since 1.0
     */
    public static @NotNull Vital api() {
        return Provider.api();
    }

    /**
     * Use reflection for the api
     * @since 1.0
     */
    public Vital() {
        try {
            Field api = Provider.class.getDeclaredField("api");
            api.setAccessible(true);
            api.set(null, this);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Gets the root directory.
     *
     * @return {@link File}
     * @since 1.0
     */
    public @NotNull abstract File getDirectory();

    /**
     * Gets the plugin {@link Logger}.
     *
     * @return {@link Logger}
     * @since 1.0
     */
    public @NotNull abstract Logger getLogger();

    /**
     * Whether we are logging or not
     *
     * @return true or false
     * @since 1.4
     */
    public abstract boolean isLogging();

    /**
     * A provider class
     * @version 1.5
     * @since 1.0
     */
    protected static final class Provider {

        private Provider() {
            throw new AssertionError();
        }

        static Vital api;

        /**
         * @return {@link Vital}
         * @since 1.0
         */
        static @NotNull Vital api() {
            return Provider.api;
        }
    }
}