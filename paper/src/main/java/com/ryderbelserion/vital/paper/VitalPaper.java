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
 * @version 1.2
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
    public VitalPaper(JavaPlugin plugin) {
        this.logger = plugin.getLogger();
        this.file = plugin.getDataFolder();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull File getDirectory() {
        return this.file;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull Logger getLogger() {
        return this.logger;
    }
}