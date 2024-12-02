package com.ryderbelserion.vital.schedulers;

import java.util.function.Consumer;

/**
 * An interface for scheduling tasks of type {@link Task}.
 * Provides methods to run tasks with various scheduling options such as delayed, repeating, and asynchronous.
 *
 * @param <Task> the type of the task being scheduled
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public interface Scheduler<Task> {

    /**
     * Schedules a task to run after a specified delay.
     *
     * @param task the task to run
     * @param delay the delay in milliseconds before the task runs
     * @return the scheduled task of type {@link Task}
     * @since 0.1.0
     */
    Task runDelayedTask(final Consumer<Scheduler<Task>> task, final long delay);

    /**
     * Schedules a repeating task to run after a specified delay.
     *
     * @param task the task to run
     * @param delay the delay in milliseconds before the task runs
     * @param interval the interval in milliseconds between successive runs
     * @return the scheduled task of type {@link Task}
     * @since 0.1.0
     */
    Task runRepeatingTask(final Consumer<Scheduler<Task>> task, final long delay, final long interval);

    /**
     * Schedules a repeating task to run at a specified interval.
     *
     * @param task the task to run
     * @param interval the interval in milliseconds between successive runs
     * @return the scheduled task of type {@link Task}
     * @since 0.1.0
     */
    Task runRepeatingTask(final Consumer<Scheduler<Task>> task, final long interval);

    /**
     * Schedules an asynchronous repeating task to run after a specified delay.
     *
     * @param task the task to run
     * @param delay the delay in milliseconds before the task runs
     * @param interval the interval in milliseconds between successive runs
     * @return the scheduled task of type {@link Task}
     * @since 0.1.0
     */
    Task runRepeatingAsyncTask(final Consumer<Scheduler<Task>> task, final long delay, final long interval);

    /**
     * Schedules an asynchronous task to run after a specified delay.
     *
     * @param task the task to run
     * @param delay the delay in milliseconds before the task runs
     * @return the scheduled task of type {@link Task}
     * @since 0.1.0
     */
    Task runDelayedAsyncTask(final Consumer<Scheduler<Task>> task, final long delay);
}