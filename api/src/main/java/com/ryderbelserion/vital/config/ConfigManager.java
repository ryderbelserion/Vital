package com.ryderbelserion.vital.config;


import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.config.keys.ConfigKeys;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.io.File;

/**
 * Handles the per plugin vital.yml config
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
@ApiStatus.Internal
public class ConfigManager {

    private static final Vital api = VitalProvider.get();
    private static final File directory = api.getDataFolder();

    private static final YamlFileResourceOptions options = YamlFileResourceOptions.builder().indentationSize(2).build();

    private static SettingsManager config;

    /**
     * Builds the vital.yml
     *
     * @author ryderbelserion
     * @since 0.0.1
     */
    public ConfigManager() {
        throw new AssertionError();
    }

    /**
     * Loads the config file.
     *
     * @since 0.0.1
     */
    @ApiStatus.Internal
    public static void load() {
        config = SettingsManagerBuilder
                .withYamlFile(new File(directory, "vital.yml"), options)
                .useDefaultMigrationService()
                .configurationData(ConfigKeys.class)
                .create();
    }

    /**
     * Reloads the config file.
     *
     * @since 0.0.1
     */
    @ApiStatus.Internal
    public static void reload() {
        config.reload();
    }

    /**
     * Saves the config file.
     *
     * @since 0.0.1
     */
    @ApiStatus.Internal
    public static void save() {
        config.save();
    }

    /**
     * Gets the config file.
     *
     * @return {@link SettingsManager}
     * @since 0.0.1
     */
    @ApiStatus.Internal
    public static @NotNull SettingsManager getConfig() {
        return config;
    }
}