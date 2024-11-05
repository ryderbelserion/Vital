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
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * A platform specific class for Paper extending {@link VitalPaper}.
 *
 * @author ryderbelserion
 * @version 2.2.4
 * @since 0.0.1
 */
public class VitalPaper extends Vital {

    private final ComponentLogger logger;
    private final File pluginsFolder;
    private final String pluginName;
    private final File dataFolder;

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
    }

    @Override
    public @NotNull String placeholders(@Nullable final Audience audience, @NotNull String line, @NotNull final Map<String, String> placeholders) {
        if (audience != null && Support.placeholder_api.isEnabled()) {
            if (audience instanceof Player player) {
                line = PlaceholderAPI.setPlaceholders(player, line);
            }
        }

        if (!placeholders.isEmpty()) {
            for (final Map.Entry<String, String> placeholder : placeholders.entrySet()) {

                if (placeholder != null) {
                    final String key = placeholder.getKey();
                    final String value = placeholder.getValue();

                    if (key != null && value != null) {
                        line = line.replace(key, value).replace(key.toLowerCase(), value);
                    }
                }
            }
        }

        return line;
    }

    @Override
    public @NotNull Component color(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return Methods.parse(placeholders(audience, line, placeholders));
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, line, placeholders));
    }

    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders) {
        sendMessage(audience, StringUtils.chomp(Methods.toString(lines)), placeholders);
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