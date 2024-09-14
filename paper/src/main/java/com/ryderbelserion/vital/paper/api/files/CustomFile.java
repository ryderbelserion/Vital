package com.ryderbelserion.vital.paper.api.files;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.common.api.Provider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * Creates {@link CustomFile}.
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class CustomFile {

    private final VitalAPI api = Provider.getApi();
    private final ComponentLogger logger = this.api.getComponentLogger();
    private final boolean isVerbose = this.api.isVerbose();

    private final String cleanName;
    private final File file;

    /**
     * A constructor to build a {@link CustomFile}.
     *
     * @param name {@link String}
     * @param file {@link File}
     * @since 0.0.1
     */
    public CustomFile(final String name, final File file) {
        this.cleanName = name.replace(".yml", "");
        this.file = file;
    }

    private YamlConfiguration configuration;

    /**
     * Loads from disk
     *
     * @return {@link CustomFile}
     * @since 0.0.1
     */
    public final CustomFile load() {
        if (this.file.isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot load, as it is a directory.");
            }

            return this;
        }

        try {
            this.configuration = CompletableFuture.supplyAsync(() -> YamlConfiguration.loadConfiguration(this.file)).join();
        } catch (Exception exception) {
            if (this.isVerbose) {
                this.logger.error("Failed to load {}", this.file.getName(), exception);
            }
        }

        return this;
    }

    /**
     * Saves to disk
     *
     * @return {@link CustomFile}
     * @since 0.0.1
     */
    public final CustomFile save() {
        if (this.file.isDirectory()) {
            if (this.isVerbose) {
                this.logger.warn("Cannot save, as it is a directory.");
            }

            return this;
        }

        if (this.configuration == null) {
            if (this.isVerbose) {
                this.logger.error("File configuration is null, cannot save!");
            }

            return this;
        }

        CompletableFuture.runAsync(() -> {
            try {
                this.configuration.save(this.file);
            } catch (Exception exception) {
                if (this.isVerbose) this.logger.error("Failed to save: {}...", this.file.getName(), exception);
            }
        });

        return this;
    }

    /**
     * Gets the {@link YamlConfiguration}
     *
     * @return {@link YamlConfiguration}
     * @since 0.0.1
     */
    public final @Nullable YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    /**
     * Checks if {@link YamlConfiguration} is null
     *
     * @return true or false
     */
    public final boolean exists() {
        return getConfiguration() != null;
    }

    /**
     * Gets the name of the file without the .yml extension.
     *
     * @return the name of the file without .yml
     * @since 0.0.1
     */
    public final String getCleanName() {
        return this.cleanName;
    }

    /**
     * Gets the {@link File}.
     *
     * @return the {@link File}
     * @since 0.0.1
     */
    public final File getFile() {
        return this.file;
    }
}