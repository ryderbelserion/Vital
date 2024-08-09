package com.ryderbelserion.vital.core.interfaces;

import java.util.function.Consumer;

public interface Scheduler {

    /**
     * Runs a task later.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     */
    void runDelayedTask(final Consumer<Scheduler> task, final long delay);

    /**
     * Runs a repeating task.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     * @param interval the interval the task runs
     */
    void runRepeatingTask(final Consumer<Scheduler> task, final long delay, final long interval);

    /**
     * Runs a repeating task.
     *
     * @param task the task consumer
     * @param interval the interval the task runs
     */
    void runRepeatingTask(final Consumer<Scheduler> task, final long interval);

    /**
     * Runs an async task.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     * @param interval the interval the task runs
     */
    void runRepeatingAsyncTask(final Consumer<Scheduler> task, final long delay, final long interval);

    /**
     * Runs an async task.
     *
     * @param task the task consumer
     * @param delay the delay until the task runs
     */
    void runDelayedAsyncTask(final Consumer<Scheduler> task, final long delay);

}