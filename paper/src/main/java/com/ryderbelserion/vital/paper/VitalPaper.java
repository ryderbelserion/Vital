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
     * Whether to log to console
     *
     * @param isLogging true or false
     */
    public void setLogging(final boolean isLogging) {
        this.isLogging = isLogging;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final boolean isLogging() {
        return this.isLogging;
    }
}