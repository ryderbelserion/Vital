package com.ryderbelserion.vital.core;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.lang.reflect.Field;

/**
 * An abstract class to extend in each platform.
 *
 * @author Ryder Belserion
 * @version 2.4.5
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
     * Use reflection for the api.
     *
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
     * Saves a single file to disk.
     *
     * @param fileName the name of the file
     * @param replace true or false
     */
    public abstract void saveResource(@NotNull final String fileName, final boolean replace);

    /**
     * Whether to use MiniMessage or not.
     *
     * @return true or false
     */
    public abstract boolean isAdventure();

    /**
     * Gets the {@link ComponentLogger}.
     * 
     * @return {@link ComponentLogger}
     */
    public abstract ComponentLogger getLogger();

    /**
     * Whether we are logging or not.
     *
     * @return true or false
     * @since 1.4
     */
    public abstract boolean isLogging();

    /**
     * A provider class
     * @version 1.6
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