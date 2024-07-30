package com.ryderbelserion.vital.paper;

import com.ryderbelserion.vital.core.Vital;
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

    private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(VitalPaper.class);

    private boolean isAdventure;
    private boolean isLogging;

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
    public void saveResource(final String fileName, final boolean replace) {
        this.plugin.saveResource(fileName, replace);
    }

    /**
     * Whether to use MiniMessage or not.
     *
     * @return true or false
     */
    @Override
    public final boolean isAdventure() {
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