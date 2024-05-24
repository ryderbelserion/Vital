package com.ryderbelserion.vital.paper;

import com.ryderbelserion.vital.core.AbstractPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.logging.Logger;

/**
 * A platform specific class for Paper extending {@link AbstractPlugin}.
 *
 * @author Ryder Belserion
 * @version 1.4
 * @since 1.1
 */
public class VitalPaper extends AbstractPlugin {

    private final Logger logger;
    private final File file;

    /**
     * A constructor to supply abstract plugin with things it needs.
     *
     * @param plugin {@link JavaPlugin}
     * @since 1.1
     */
    public VitalPaper(@NotNull final JavaPlugin plugin) {
        this.logger = plugin.getLogger();
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
    public @NotNull final Logger getLogger() {
        return this.logger;
    }

    @Override
    public final boolean isLogging() {
        return false;
    }
}