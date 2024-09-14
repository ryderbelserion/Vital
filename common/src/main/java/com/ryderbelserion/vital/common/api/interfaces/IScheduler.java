package com.ryderbelserion.vital.common.api.interfaces;

import java.util.function.Consumer;

/**
 * An abstract class to extend in each platform for running tasks.
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public interface IScheduler {

    /**
     * Runs a task later.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     * @since 0.0.1
     */
    void runDelayedTask(final Consumer<IScheduler> task, final long delay);

    /**
     * Runs a repeating task.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     * @param interval the interval the task runs
     * @since 0.0.1
     */
    void runRepeatingTask(final Consumer<IScheduler> task, final long delay, final long interval);

    /**
     * Runs a repeating task.
     *
     * @param task the task consumer
     * @param interval the interval the task runs
     * @since 0.0.1
     */
    void runRepeatingTask(final Consumer<IScheduler> task, final long interval);

    /**
     * Runs an async task.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     * @param interval the interval the task runs
     * @since 0.0.1
     */
    void runRepeatingAsyncTask(final Consumer<IScheduler> task, final long delay, final long interval);

    /**
     * Runs an async task.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     * @since 0.0.1
     */
    void runDelayedAsyncTask(final Consumer<IScheduler> task, final long delay);

}