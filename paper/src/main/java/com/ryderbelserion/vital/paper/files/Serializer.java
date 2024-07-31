package com.ryderbelserion.vital.paper.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.CompletableFuture;

/**
 * A class to create a json file used for data storage.
 *
 * @author Ryder Belserion
 * @version 2.4.5
 * @since 1.5.5
 */
public class Serializer {

    private static final GsonBuilder builder = new GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization();

    private final File file;

    private Gson gson = null;

    /**
     * Creates a serializer instance to create a json file.
     *
     * @param file the {@link File}
     */
    public Serializer(@NotNull final File file) {
        this.file = file;
    }

    /**
     * Excludes variables with the {@link com.google.gson.annotations.Expose} annotation from being parsed.
     */
    public void withoutExposeAnnotation() {
        builder.excludeFieldsWithoutExposeAnnotation();
    }

    /**
     * Excludes variables with specific modifier annotations from being parsed.
     *
     * @param modifiers the {@link Modifier}
     */
    public void withoutModifiers(final int... modifiers) {
        builder.excludeFieldsWithModifiers(modifiers);
    }

    /**
     * Builds everything we need
     */
    public void build() {
        if (this.gson == null) {
            this.gson = builder.create();
        }

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Create the {@link File} if it does not exist and writes/reads data.
     */
    public void apply() {
        if (exists()) {
            save();

            return;
        }

        read();
    }

    /**
     * Checks if the file exists.
     *
     * @return true or false
     */
    public final boolean exists() {
        return this.file.exists();
    }

    /**
     * Writes to {@link File} with the content.
     */
    public void save() {
        CompletableFuture.runAsync(() -> {
            try (final FileWriter writer = new FileWriter(this.file)) {
                writer.write(this.gson.toJson(this));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Reads the file.
     */
    private void read() {
        CompletableFuture.runAsync(() -> {
            try (final BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
                this.gson.fromJson(reader, getClass());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}