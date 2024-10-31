package com.ryderbelserion.vital.paper;

import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.paper.api.enums.Support;
import com.ryderbelserion.vital.paper.api.files.FileManager;
import com.ryderbelserion.vital.utils.Methods;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Map;

/**
 * A platform specific class for Paper extending {@link VitalPaper}.
 *
 * @author ryderbelserion
 * @version 2.2.3
 * @since 0.0.1
 */
public class VitalPaper implements Vital {

    private final ComponentLogger logger;
    private final File pluginsFolder;
    private final String pluginName;
    private final File dataFolder;

    private final FileManager fileManager;

    /**
     * Builds an instance of VitalPaper at JavaPlugin creation.
     *
     * @param plugin {@link JavaPlugin} context
     * @since 2.0.0
     */
    public VitalPaper(final JavaPlugin plugin) {
        this.pluginsFolder = plugin.getServer().getPluginsFolder();
        this.logger = plugin.getComponentLogger();
        this.dataFolder = plugin.getDataFolder();
        this.pluginName = plugin.getName();

        start();

        this.fileManager = new FileManager();
    }

    /**
     * Builds an instance of VitalPaper at bootstrap
     *
     * @param context {@link BootstrapContext} context
     * @since 2.0.0
     */
    public VitalPaper(final BootstrapContext context) {
        this.pluginsFolder = context.getDataDirectory().getParent().toFile();
        this.logger = context.getLogger();
        this.dataFolder = context.getDataDirectory().toFile();
        this.pluginName = context.getPluginMeta().getName();

        start();

        this.fileManager = new FileManager();
    }

    @Override
    public @NotNull String placeholders(@NotNull final Audience audience, @NotNull String value, @NotNull final Map<String, String> placeholders) {
        if (Support.placeholder_api.isEnabled()) {
            if (audience instanceof Player player) {
                value = PlaceholderAPI.setPlaceholders(player, value);
            }
        }

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> placeholder : placeholders.entrySet()) {

                if (placeholder != null) {
                    final String key = placeholder.getKey();
                    final String line = placeholder.getValue();

                    if (key != null && line != null) {
                        value = value.replace(key, value).replace(key.toLowerCase(), value);
                    }
                }
            }
        }

        return value;
    }

    @Override
    public @NotNull Component color(@NotNull final Audience audience, @NotNull final String value, @NotNull final Map<String, String> placeholders) {
        return Methods.parse(placeholders(audience, value, placeholders));
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final String value, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, value, placeholders));
    }

    @Override
    public FileManager getFileManager() {
        return this.fileManager;
    }

    @Override
    public ComponentLogger getLogger() {
        return this.logger;
    }

    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }

    @Override
    public String getPluginName() {
        return this.pluginName;
    }

    @Override
    public File getPluginsFolder() {
        return this.pluginsFolder;
    }
}