package com.ryderbelserion.vital.paper.util.scheduler;

import com.ryderbelserion.vital.schedulers.Scheduler;
import com.ryderbelserion.vital.paper.util.scheduler.impl.FoliaScheduler;
import com.ryderbelserion.vital.schedulers.enums.SchedulerType;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * A class to handle scheduling tasks for Paper servers
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class PaperScheduler implements Scheduler<ScheduledTask> {

    private final JavaPlugin plugin;

    /**
     * Builds the paper scheduler object
     *
     * @param plugin the plugin instance
     * @since 0.1.0
     */
    public PaperScheduler(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public ScheduledTask runDelayedTask(@NotNull final Consumer<Scheduler<ScheduledTask>> task, final long delay) {
        return new FoliaScheduler(this.plugin, SchedulerType.global_scheduler) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runDelayed(delay);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     * @param interval {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public ScheduledTask runRepeatingTask(@NotNull final Consumer<Scheduler<ScheduledTask>> task, final long delay, final long interval) {
        return new FoliaScheduler(this.plugin, SchedulerType.global_scheduler) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runAtFixedRate(delay, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param interval {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public ScheduledTask runRepeatingTask(@NotNull final Consumer<Scheduler<ScheduledTask>> task, final long interval) {
        return new FoliaScheduler(this.plugin, SchedulerType.global_scheduler) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runAtFixedRate(0L, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     * @param interval {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public ScheduledTask runRepeatingAsyncTask(@NotNull final Consumer<Scheduler<ScheduledTask>> task, final long delay, final long interval) {
        return new FoliaScheduler(this.plugin, SchedulerType.async_scheduler) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runAtFixedRate(delay, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public ScheduledTask runDelayedAsyncTask(@NotNull final Consumer<Scheduler<ScheduledTask>> task, final long delay) {
        return new FoliaScheduler(this.plugin, SchedulerType.async_scheduler) {
            @Override
            public void run() {
                task.accept(PaperScheduler.this);
            }
        }.runDelayed(delay);
    }
}