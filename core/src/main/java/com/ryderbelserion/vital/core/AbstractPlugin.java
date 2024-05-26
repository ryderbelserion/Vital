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
public abstract class AbstractPlugin {

    /**
     * @return {@link AbstractPlugin}
     * @since 1.0
     */
    public static @NotNull AbstractPlugin api() {
        return Provider.api();
    }

    /**
     * Use reflection for the api
     * @since 1.0
     */
    public AbstractPlugin() {
        try {
            Field api = Provider.class.getDeclaredField("api");
            api.setAccessible(true);
            api.set(null, this);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * @return {@link File}
     * @since 1.0
     */
    public @NotNull abstract File getDirectory();

    /**
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

    protected static final class Provider {
        static AbstractPlugin api;

        /**
         * @return {@link AbstractPlugin}
         * @since 1.0
         */
        static @NotNull AbstractPlugin api() {
            return Provider.api;
        }
    }
}