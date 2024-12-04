package com.ryderbelserion.vital.paper;

import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.paper.api.enums.Support;
import com.ryderbelserion.vital.paper.api.files.PaperFileManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A platform-specific class for Paper extending {@link Vital}.
 * This class provides methods and attributes specific to the Paper server implementation.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class VitalPaper extends Vital {

    private final ComponentLogger logger;
    private final File pluginsFolder;
    private final String pluginName;
    private final File dataFolder;

    private final PaperFileManager paperFileManager;

    /**
     * Constructs an instance of VitalPaper using a {@link JavaPlugin}.
     * Initializes the logger, plugins folder, data folder, and plugin name from the provided plugin context.
     *
     * @param plugin the {@link JavaPlugin} context
     * @since 0.1.0
     */
    public VitalPaper(final JavaPlugin plugin) {
        this.pluginsFolder = plugin.getServer().getPluginsFolder();
        this.logger = plugin.getComponentLogger();
        this.dataFolder = plugin.getDataFolder();
        this.pluginName = plugin.getName();

        this.paperFileManager = new PaperFileManager();

        start();
    }

    /**
     * Constructs an instance of VitalPaper using a {@link BootstrapContext}.
     * Initializes the logger, plugins folder, data folder, and plugin name from the provided bootstrap context.
     *
     * @param context the {@link BootstrapContext} context
     * @since 0.1.0
     */
    public VitalPaper(final BootstrapContext context) {
        this.pluginsFolder = context.getDataDirectory().getParent().toFile();
        this.logger = context.getLogger();
        this.dataFolder = context.getDataDirectory().toFile();
        this.pluginName = context.getPluginMeta().getName();

        this.paperFileManager = new PaperFileManager();

        start();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public File getPluginsFolder() {
        return this.pluginsFolder;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public File getDataFolder() {
        return this.dataFolder;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public String getPluginName() {
        return this.pluginName;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public ComponentLogger getLogger() {
        return this.logger;
    }

    /**
     * {@inheritDoc}
     *
     * @param line {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull String placeholders(@NotNull String line) {
        return placeholders(null, line, new HashMap<>());
    }

    /**
     * {@inheritDoc}
     *
     * @param line {@inheritDoc}
     * @param placeholders {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull String placeholders(@NotNull String line, @NotNull Map<String, String> placeholders) {
        return placeholders(null, line, placeholders);
    }

    /**
     * {@inheritDoc}
     *
     * @param audience {@inheritDoc}
     * @param line {@inheritDoc}
     * @param placeholders {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
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

    /**
     * {@inheritDoc}
     *
     * @param line {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull Component color(@NotNull String line) {
        return color(null, line, new HashMap<>());
    }

    /**
     * {@inheritDoc}
     *
     * @param line {@inheritDoc}
     * @param placeholders {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull Component color(@NotNull String line, @NotNull Map<String, String> placeholders) {
        return color(null, line, placeholders);
    }

    /**
     * {@inheritDoc}
     *
     * @param audience {@inheritDoc}
     * @param line {@inheritDoc}
     * @param placeholders {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull Component color(@Nullable final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        return Methods.parse(placeholders(audience, line, placeholders));
    }

    /**
     * {@inheritDoc}
     *
     * @param audience {@inheritDoc}
     * @param line {@inheritDoc}
     * @param placeholders {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final String line, @NotNull final Map<String, String> placeholders) {
        audience.sendMessage(color(audience, line, placeholders));
    }

    /**
     * {@inheritDoc}
     *
     * @param audience {@inheritDoc}
     * @param lines {@inheritDoc}
     * @param placeholders {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void sendMessage(@NotNull final Audience audience, @NotNull final List<String> lines, @NotNull final Map<String, String> placeholders) {
        sendMessage(audience, StringUtils.chomp(Methods.toString(lines)), placeholders);
    }

    /**
     * Gets the FileManager.
     *
     * @return the FileManager
     * @since 0.1.0
     */
    public final PaperFileManager getFileManager() {
        return this.paperFileManager;
    }
}