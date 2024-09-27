package com.ryderbelserion.vital.paper.util.scheduler;

import com.ryderbelserion.vital.common.api.interfaces.IScheduler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * A class to handle scheduling tasks for Paper servers
 *
 * @author ryderbelserion
 * @version 0.0.9
 * @since 0.0.1
 */
public class PaperScheduler implements IScheduler {

    private final JavaPlugin plugin;

    /**
     * Builds the paper scheduler object
     *
     * @param plugin the plugin instance
     * @since 0.0.1
     */
    public PaperScheduler(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     */
    @Override
    public void runDelayedTask(@NotNull final Consumer<IScheduler> task, final long delay) {
        new FoliaRunnable(this.plugin.getServer().getGlobalRegionScheduler()) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runDelayed(this.plugin, delay);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     * @param interval {@inheritDoc}
     */
    @Override
    public void runRepeatingTask(@NotNull final Consumer<IScheduler> task, final long delay, final long interval) {
        new FoliaRunnable(this.plugin.getServer().getGlobalRegionScheduler()) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runAtFixedRate(this.plugin, delay, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param interval {@inheritDoc}
     */
    @Override
    public void runRepeatingTask(@NotNull final Consumer<IScheduler> task, final long interval) {
        new FoliaRunnable(this.plugin.getServer().getGlobalRegionScheduler()) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runAtFixedRate(this.plugin, 0L, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     * @param interval {@inheritDoc}
     */
    @Override
    public void runRepeatingAsyncTask(@NotNull final Consumer<IScheduler> task, final long delay, final long interval) {
        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runAtFixedRate(this.plugin, delay, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     */
    @Override
    public void runDelayedAsyncTask(@NotNull final Consumer<IScheduler> task, final long delay) {
        new FoliaRunnable(this.plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runDelayed(this.plugin, delay);
    }
}