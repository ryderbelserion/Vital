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
 * @version 2.0.1
 * @since 1.1
 */
public class VitalPaper extends Vital {

    private final JavaPlugin plugin;
    private boolean isAdventure;
    private boolean isLogging;

    /**
     * A constructor to supply abstract plugin with things it needs.
     *
     * @param plugin {@link JavaPlugin}
     * @since 1.1
     */
    public VitalPaper(@NotNull final JavaPlugin plugin, final boolean isAdventure) {
        this.plugin = plugin;
        this.isAdventure = isAdventure;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final File getDirectory() {
        return this.plugin.getDataFolder();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final ComponentLogger getLogger() {
        return this.plugin.getComponentLogger();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void saveResource(final String fileName, final boolean replace) {
        this.plugin.saveResource(fileName, replace);
    }

    /**
     * Whether to use MiniMessage or not.
     *
     * @return true or false
     */
    @Override
    public boolean isAdventure() {
        return this.isAdventure;
    }

    /**
     * Whether to log to console
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
     */
    @Override
    public final boolean isLogging() {
        return this.isLogging;
    }
}