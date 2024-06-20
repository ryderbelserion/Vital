package com.ryderbelserion.vital.paper;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.logging.Logger;

/**
 * A platform specific class for Paper extending {@link Vital}.
 *
 * @author Ryder Belserion
 * @version 1.6
 * @since 1.1
 */
public class VitalPaper extends Vital {

    private final PaperCommandManager commandManager;

    private final JavaPlugin plugin;
    private final Logger logger;
    private boolean isLogging;
    private final File file;

    /**
     * A constructor to supply abstract plugin with things it needs.
     *
     * @param plugin {@link JavaPlugin}
     * @since 1.1
     */
    public VitalPaper(@NotNull final JavaPlugin plugin) {
        this.commandManager = new PaperCommandManager();

        this.file = plugin.getDataFolder();
        this.logger = plugin.getLogger();
        this.plugin = plugin;
    }

    public @NotNull final PaperCommandManager getCommandManager() {
        return this.commandManager;
    }

    public @NotNull final JavaPlugin getPlugin() {
        return this.plugin;
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