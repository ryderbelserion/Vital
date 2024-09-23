package com.ryderbelserion.vital.velocity;

import com.ryderbelserion.vital.velocity.modules.EventRegistry;
import com.ryderbelserion.vital.velocity.modules.ModuleLoader;
import com.ryderbelserion.vital.velocity.util.scheduler.VeloScheduler;
import com.ryderbelserion.vital.common.VitalAPI;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

/**
 * A platform specific class for Velocity extending {@link VitalAPI}.
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public abstract class Vital implements VitalAPI {

    private final ComponentLogger logger;
    private final ProxyServer server;
    private final String pluginName;
    private final File directory;

    /**
     * Builds the vital velocity constructor
     *
     * @param server {@link ProxyServer}
     * @param logger {@link ComponentLogger}
     * @param pluginName name of the plugin
     * @param directory {@link Path}
     */
    public Vital(@NotNull final ProxyServer server, @NotNull final ComponentLogger logger, @NotNull final Path directory, @NotNull final String pluginName) {
        this.directory = directory.toFile();
        this.directory.mkdirs();

        this.pluginName = pluginName;

        this.server = server;
        this.logger = logger;

        start();
    }

    private PluginContainer container;
    private VeloScheduler scheduler;
    private ModuleLoader loader;

    /**
     * {@inheritDoc}
     *
     * @since 0.0.1
     */
    @Override
    public void start() {
        VitalAPI.super.start();

        final Optional<PluginContainer> ploogin = this.server.getPluginManager().getPlugin(getPluginName());
        ploogin.ifPresent(container -> this.container = container);

        this.loader = new ModuleLoader(new EventRegistry(this.container, this.server));
        this.scheduler = new VeloScheduler(this.container, this.server);

        getDirectory().mkdirs();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public final File getDirectory() {
        return this.directory;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final File getModsDirectory() {
        return getDirectory().getParentFile();
    }

    /**
     * {@inheritDoc}
     *
     * @param fileName {@inheritDoc}
     * @param replace {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public void saveResource(@NotNull final String fileName, final boolean replace) {}

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public final ComponentLogger getComponentLogger() {
        return this.logger;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public final VeloScheduler getScheduler() {
        return this.scheduler;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public final String getPluginName() {
        return this.pluginName;
    }

    /**
     * Gets the {@link ModuleLoader}
     *
     * @return {@link ModuleLoader}
     * @since 0.0.1
     */
    public final ModuleLoader getLoader() {
        return this.loader;
    }
}