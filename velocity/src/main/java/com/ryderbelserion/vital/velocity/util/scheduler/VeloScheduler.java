package com.ryderbelserion.vital.velocity.util.scheduler;

import com.ryderbelserion.vital.common.api.interfaces.IScheduler;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A class to handle scheduling tasks for Velocity servers
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public class VeloScheduler implements IScheduler {

    private final PluginContainer container;
    private final ProxyServer server;

    /**
     * Creates a velocity scheduler
     *
     * @param container {@link PluginContainer}
     * @param server {@link ProxyServer}
     *
     * @since 0.0.1
     */
    public VeloScheduler(@NotNull final PluginContainer container, @NotNull final ProxyServer server) {
        this.container = container;
        this.server = server;
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     */
    @Override
    public void runDelayedTask(@NotNull final Consumer<IScheduler> task, final long delay) {
        this.server.getScheduler().buildTask(this.container, () -> task.accept(this)).delay(delay, TimeUnit.SECONDS).schedule();
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
        runRepeatingTask(task, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param interval {@inheritDoc}
     */
    @Override
    public void runRepeatingTask(@NotNull final Consumer<IScheduler> task, final long interval) {
        this.server.getScheduler().buildTask(this.container, () -> task.accept(this)).repeat(interval, TimeUnit.SECONDS).schedule();
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
        runRepeatingTask(task, delay, interval);
    }

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @param delay {@inheritDoc}
     */
    @Override
    public void runDelayedAsyncTask(@NotNull final Consumer<IScheduler> task, final long delay) {
        runDelayedTask(task, delay);
    }
}