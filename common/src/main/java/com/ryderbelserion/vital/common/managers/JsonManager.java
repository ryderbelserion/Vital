package com.ryderbelserion.vital.common.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;
import com.ryderbelserion.vital.common.api.Provider;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A class to create a json file used for data storage.
 *
 * @author Ryder Belserion
 * @param <T> the class
 * @version 0.0.1
 * @since 0.0.1
 */
public class JsonManager<T> {

    private static final Map<String, Lock> locks = new HashMap<>();

    private final GsonBuilder builder = Provider.getApi().getBuilder() == null ? new GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization() : Provider.getApi().getBuilder();

    private final File file;
    private final T clazz;

    private Gson gson = null;

    /**
     * Creates a serializer instance to create a json file.
     *
     * @param file the {@link File}
     * @param clazz {@link T}
     * @since 0.0.1
     */
    public JsonManager(@NotNull final File file, T clazz) {
        this.file = file;
        this.clazz = clazz;
    }

    /**
     * Excludes variables with the {@link Expose} annotation from being parsed.
     *
     * @return {@link JsonManager}
     * @since 0.0.1
     */
    public final JsonManager<T> withoutExposeAnnotation() {
        this.builder.excludeFieldsWithoutExposeAnnotation();

        return this;
    }

    /**
     * Set pretty printing
     *
     * @return {@link JsonManager}
     * @since 0.0.1
     */
    public final JsonManager<T> setPrettyPrinting() {
        this.builder.setPrettyPrinting();

        return this;
    }

    /**
     * Excludes variables with specific modifier annotations from being parsed.
     *
     * @param modifiers the {@link Modifier}
     * @return {@link JsonManager}
     * @since 0.0.1
     */
    public final JsonManager<T> withoutModifiers(final int... modifiers) {
        this.builder.excludeFieldsWithModifiers(modifiers);

        return this;
    }

    /**
     * Registers a type adapter
     *
     * @param type the type adapter
     * @param object the object
     * @return {@link JsonManager}
     * @since 0.0.1
     */
    public final JsonManager<T> registerAdapters(final Type type, final Object object) {
        this.builder.registerTypeAdapter(type, object);

        return this;
    }

    /**
     * Builds everything we need
     *
     * @return {@link JsonManager}
     * @since 0.0.1
     */
    public final JsonManager<T> load() {
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
     * @return true or false
     * @since 0.0.1
     */
    public final boolean exists() {
        return this.file.exists();
    }

    /**
     * Writes to {@link File} with the content.
     *
     * @since 0.0.1
     */
    public void write() {
        Lock lock;

        if (locks.containsKey(this.file.getName())) {
            lock = locks.get(this.file.getName());
        } else {
            locks.put(this.file.getName(), lock = new ReentrantReadWriteLock().writeLock());
        }

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
     * Reads the file.
     *
     * @since 0.0.1
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
            Provider.getApi().getComponentLogger().warn("Cannot read from file as object is null, File: {}", this.file.getName());

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