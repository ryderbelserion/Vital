package com.ryderbelserion.vital.paper.util.scheduler.impl;

import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.schedulers.enums.SchedulerType;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Handles support for Folia/Paper.
 *
 * @author Ryder Belserion
 * @author Euphyllia
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class FoliaScheduler implements Runnable {

    private final JavaPlugin plugin;
    private final SchedulerType type;
    private final Server server;
    private final TimeUnit timeUnit;

    private ScheduledTask task;

    /**
     * Constructs a Folia Scheduler with the specified plugin, schedule type, and time unit.
     *
     * @param plugin the plugin
     * @param type the schedule type
     * @param timeUnit the time unit
     * @since 0.1.0
     */
    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final SchedulerType type, @NotNull final TimeUnit timeUnit) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.timeUnit = timeUnit;
        this.type = type;
    }

    /**
     * Constructs a Folia Scheduler with the specified plugin and schedule type, using seconds as the time unit.
     *
     * @param plugin the plugin
     * @param type the schedule type
     * @since 0.1.0
     */
    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final SchedulerType type) {
        this(plugin, type, TimeUnit.SECONDS);
    }

    private World world;
    private int x;
    private int z;

    /**
     * Constructs a Folia Scheduler with the specified plugin, world, and coordinates for region scheduling.
     *
     * @param plugin the plugin
     * @param world the world
     * @param x the x coordinate
     * @param z the z coordinate
     * @since 0.1.0
     */
    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final World world, final int x, final int z) {
        this(plugin, SchedulerType.region_scheduler, TimeUnit.SECONDS);

        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Constructs a Folia Scheduler with the specified plugin and location for region scheduling.
     *
     * @param plugin the plugin
     * @param location the location
     * @since 0.1.0
     */
    public FoliaScheduler(@NotNull final JavaPlugin plugin, @NotNull final Location location) {
        this(plugin, location.getWorld(), location.getBlockX(), location.getBlockZ());
    }

    private Runnable retired;
    private Entity entity;

    /**
     * Constructs a Folia Scheduler with the specified plugin, retired runnable, and entity for entity scheduling.
     *
     * @param plugin the plugin
     * @param retired the retired runnable
     * @param entity the entity
     * @since 0.1.0
     */
    public FoliaScheduler(@NotNull final JavaPlugin plugin, @Nullable final Runnable retired, @NotNull final Entity entity) {
        this(plugin, SchedulerType.entity_scheduler);

        this.retired = retired;
        this.entity = entity;
    }

    /**
     * Runs the task immediately.
     *
     * @return the scheduled task
     * @since 0.1.0
     */
    public ScheduledTask runNow() {
        isScheduled();

        ScheduledTask task = null;

        switch (this.type) {
            case global_scheduler -> task = this.server.getGlobalRegionScheduler().run(this.plugin, scheduledTask -> this.run());
            case async_scheduler -> task = this.server.getAsyncScheduler().runNow(this.plugin, scheduledTask -> this.run());
            case region_scheduler -> task = this.server.getRegionScheduler().run(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run());
            case entity_scheduler -> task = this.entity.getScheduler().run(this.plugin, scheduledTask -> this.run(), this.retired);
        }

        return this.task = task;
    }

    /**
     * Runs the task after a delay.
     *
     * @param delay the delay in milliseconds
     * @return the scheduled task
     * @since 0.1.0
     */
    public ScheduledTask runDelayed(final long delay) {
        isScheduled();

        ScheduledTask task = null;

        switch (this.type) {
            case global_scheduler -> task = this.server.getGlobalRegionScheduler().runDelayed(this.plugin, scheduledTask -> this.run(), delay);
            case async_scheduler -> task = this.server.getAsyncScheduler().runDelayed(this.plugin, scheduledTask -> this.run(), delay, this.timeUnit);
            case region_scheduler -> task = this.server.getRegionScheduler().runDelayed(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run(), delay);
            case entity_scheduler -> task = this.entity.getScheduler().runDelayed(this.plugin, scheduledTask -> this.run(), this.retired, delay);
        }

        return this.task = task;
    }

    /**
     * Runs the task at a fixed rate.
     *
     * @param delay the initial delay in milliseconds
     * @param interval the interval in milliseconds
     * @return the scheduled task
     * @since 0.1.0
     */
    public ScheduledTask runAtFixedRate(final long delay, final long interval) {
        isScheduled();

        ScheduledTask task = null;

        switch (this.type) {
            case global_scheduler -> task = this.server.getGlobalRegionScheduler().runAtFixedRate(this.plugin, scheduledTask -> this.run(), delay, interval);
            case async_scheduler -> task = this.server.getAsyncScheduler().runAtFixedRate(this.plugin, scheduledTask -> this.run(), delay, interval, this.timeUnit);
            case region_scheduler -> task = this.server.getRegionScheduler().runAtFixedRate(this.plugin, this.world, this.x, this.z, scheduledTask -> this.run(), delay, interval);
            case entity_scheduler -> task = this.entity.getScheduler().runAtFixedRate(this.plugin, scheduledTask -> this.run(), this.retired, delay, interval);
        }

        return this.task = task;
    }

    /**
     * Cancels the task and executes an optional consumer.
     *
     * @param consumer the consumer to execute on cancel
     * @since 0.1.0
     */
    public void cancel(@Nullable final Consumer<FoliaScheduler> consumer) {
        isNotScheduled();

        this.task.cancel();

        if (consumer != null) {
            consumer.accept(this);
        }
    }

    /**
     * Cancels the task.
     *
     * @since 0.1.0
     */
    public void cancel() {
        cancel(null);
    }

    /**
     * The method executed in the tasks.
     *
     * @since 0.1.0
     */
    @Override
    public void run() {}

    /**
     * Gets the task ID.
     *
     * @return the task ID
     * @throws GenericException if the task is not scheduled
     * @since 0.1.0
     */
    public final int getTaskId() throws GenericException {
        isNotScheduled(); // throws an exception if task is null.

        return this.task.hashCode();
    }

    /**
     * Gets the schedule type.
     *
     * @return the schedule type
     * @since 0.1.0
     */
    public final SchedulerType getType() {
        return this.type;
    }

    /**
     * Gets the scheduled task.
     *
     * @return the scheduled task
     * @since 0.1.0
     */
    public final ScheduledTask getTask() {
        return this.task;
    }

    private void isNotScheduled() {
        if (this.task == null) throw new GenericException("The task is not yet scheduled.");
    }

    private void isScheduled() {
        if (this.task != null) throw new GenericException("The task is already scheduled as " + this.task.hashCode());
    }
}