package com.ryderbelserion.vital.paper;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.paper.modules.EventRegistry;
import com.ryderbelserion.vital.paper.modules.ModuleLoader;
import com.ryderbelserion.vital.paper.api.files.FileManager;
import com.ryderbelserion.vital.paper.util.scheduler.PaperScheduler;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;

/**
 * A platform specific class for Paper extending {@link VitalAPI}.
 *
 * @author ryderbelserion
 * @version 1.0.7
 * @since 0.0.1
 */
public abstract class Vital extends JavaPlugin implements VitalAPI {

    private PaperScheduler scheduler;
    private FileManager fileManager;
    private ModuleLoader loader;

    /**
     * Builds the vital paper constructor
     *
     * @author ryderbelserion
     * @since 0.0.1
     */
    public Vital() {
        start();
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.0.1
     */
    @Override
    public void start() {
        VitalAPI.super.start();

        this.loader = new ModuleLoader(new EventRegistry(this));
        this.scheduler = new PaperScheduler(this);
        this.fileManager = new FileManager();

        getDirectory().mkdirs();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public File getDirectory() {
        return getDataFolder();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public FileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public File getModsDirectory() {
        return getServer().getPluginsFolder();
    }

    /**
     * {@inheritDoc}
     *
     * @param fileName {@inheritDoc}
     * @param replace {@inheritDoc}
     */
    @Override
    public void saveResource(@NotNull final String fileName, final boolean replace) {
        super.saveResource(fileName, replace);
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public @NotNull ComponentLogger getComponentLogger() {
        return super.getComponentLogger();
    }
    
    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public PaperScheduler getScheduler() {
        return this.scheduler;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public String getPluginName() {
        return getName();
    }

    /**
     * Gets the {@link ModuleLoader}
     *
     * @return {@link ModuleLoader}
     * @since 0.0.1
     */
    public ModuleLoader getLoader() {
        return this.loader;
    }
}