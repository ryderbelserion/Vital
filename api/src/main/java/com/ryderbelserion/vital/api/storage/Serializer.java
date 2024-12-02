package com.ryderbelserion.vital.api.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A class to create a JSON file for data storage.
 *
 * @param <T> the class type
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class Serializer<T> {

    private static final Map<String, Lock> locks = new HashMap<>();

    private final Vital vital = VitalProvider.get();

    private final GsonBuilder builder = this.vital.getGson() == null ? new GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization() : this.vital.getGson();

    private final File file;
    private final T clazz;
    private Gson gson = null;

    /**
     * Creates a serializer instance for a JSON file.
     *
     * @param file the file
     * @param clazz the class type
     * @since 0.1.0
     */
    public Serializer(@NotNull final File file, @NotNull final T clazz) {
        this.file = file;
        this.clazz = clazz;
    }

    /**
     * Excludes fields without the {@link Expose} annotation.
     *
     * @return the serializer instance
     * @since 0.1.0
     */
    public final Serializer<T> withoutExposeAnnotation() {
        this.builder.excludeFieldsWithoutExposeAnnotation();

        return this;
    }

    /**
     * Enables pretty printing.
     *
     * @return the serializer instance
     * @since 0.1.0
     */
    public final Serializer<T> setPrettyPrinting() {
        this.builder.setPrettyPrinting();

        return this;
    }

    /**
     * Excludes fields with specific modifiers.
     *
     * @param modifiers the modifiers
     * @return the serializer instance
     * @since 0.1.0
     */
    public final Serializer<T> withoutModifiers(final int... modifiers) {
        this.builder.excludeFieldsWithModifiers(modifiers);

        return this;
    }

    /**
     * Registers type adapters.
     *
     * @param type the type
     * @param object the adapter object
     * @return the serializer instance
     * @since 0.1.0
     */
    public final Serializer<T> registerAdapters(@NotNull final Type type, @NotNull final Object object) {
        this.builder.registerTypeAdapter(type, object);

        return this;
    }

    /**
     * Loads the serializer and creates the file if it doesn't exist.
     *
     * @return the serializer instance
     * @since 0.1.0
     */
    public final Serializer<T> load() {
        if (this.gson == null) {
            this.gson = this.builder.create();
        }

        if (!exists()) {
            try {
                this.file.createNewFile();
                write();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            read();
        }

        return this;
    }

    /**
     * Checks if the file exists.
     *
     * @return true if the file exists, false otherwise
     * @since 0.1.0
     */
    public final boolean exists() {
        return this.file.exists();
    }

    /**
     * Writes the content to the file.
     *
     * @since 0.1.0
     */
    public void write() {
        final Lock lock = locks.computeIfAbsent(this.file.getName(), k -> new ReentrantReadWriteLock().writeLock());

        CompletableFuture.runAsync(() -> {
            try (final FileWriter writer = new FileWriter(this.file)) {
                writer.write(this.gson.toJson(this.clazz));
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                lock.unlock();

                locks.remove(this.file.getName());
            }
        });
    }

    /**
     * Reads the content from the file.
     *
     * @since 0.1.0
     */
    public void read() {
        JsonObject object = CompletableFuture.supplyAsync(() -> {
            try (final FileReader reader = new FileReader(this.file)) {
                return JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return null;
        }).join();

        if (object == null) {
            this.vital.getLogger().warn("Cannot read from file as object is null, File: {}", this.file.getName());

            return;
        }

        for (java.lang.reflect.Field field : this.clazz.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Expose.class)) {
                field.setAccessible(true);
                JsonElement jsonElement = object.get(field.getName());

                if (jsonElement != null) {
                    try {
                        field.set(null, this.gson.fromJson(jsonElement, field.getType()));
                    } catch (IllegalAccessException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }
    }
}