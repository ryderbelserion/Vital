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
 * @version 1.8.4
 * @since 1.1
 */
public class VitalPaper extends Vital {

    private final ComponentLogger logger;
    private boolean isLogging;
    private final File file;

    /**
     * A constructor to supply abstract plugin with things it needs.
     *
     * @param plugin {@link JavaPlugin}
     * @since 1.1
     */
    public VitalPaper(@NotNull final JavaPlugin plugin) {
        this.logger = plugin.getComponentLogger();
        this.file = plugin.getDataFolder();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final File getDirectory() {
        return this.file;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final ComponentLogger getLogger() {
        return this.logger;
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