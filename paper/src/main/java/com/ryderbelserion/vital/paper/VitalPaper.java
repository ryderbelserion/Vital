package com.ryderbelserion.vital.paper;

import com.ryderbelserion.vital.core.Vital;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;

/**
 * A platform specific class for Paper extending {@link Vital}.
 *
 * @author Ryder Belserion
 * @version 2.4.5
 * @since 1.1
 */
public class VitalPaper extends Vital {

    private final JavaPlugin plugin;

    private boolean isAdventure;
    private boolean isLogging;
    
    /**
     * Builds the vital paper constructor
     *
     * @param plugin {@link JavaPlugin}
     */
    public VitalPaper(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public @NotNull final File getDirectory() {
        return this.plugin.getDataFolder();
    }

    /**
     * {@inheritDoc}
     *
     * @param fileName {@inheritDoc}
     * @param replace {@inheritDoc}
     */
    @Override
    public void saveResource(@NotNull final String fileName, final boolean replace) {
        this.plugin.saveResource(fileName, replace);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final boolean isAdventure() {
        return this.isAdventure;
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public final ComponentLogger getLogger() {
        return this.plugin.getComponentLogger();
    }

    /**
     * Whether to log to console.
     *
     * @param isLogging true or false
     */
    public void setLogging(final boolean isLogging) {
        this.isLogging = isLogging;
    }

    /**
     * Sets whether to be adventure api or not.
     *
     * @param isAdventure true or false
     */
    public void setAdventure(final boolean isAdventure) {
        this.isAdventure = isAdventure;
    }

    /**
     {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final boolean isLogging() {
        return this.isLogging;
    }
}