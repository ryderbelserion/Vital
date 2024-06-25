package com.ryderbelserion.vital.discord.util.scheduler

import java.time.LocalDateTime

/**
 * A schedule that is based on a second timer.
 */
public class ScheduleTimer(
    private val period: Long,
    private val delay: Long,
    override val isRepeating: Boolean = true,
    private val block: suspend () -> Unit,
) : Schedule {

    private var delayOver = false
    private var counter = 0L

    override suspend fun execute() {
        this.block()
    }

    override fun shouldRun(nowDateTime: LocalDateTime): Boolean {
        this.counter++

        if (!this.delayOver && (this.delay == 0L || this.counter % this.delay == 0L)) {
            this.delayOver = true

            return true
        }

        if (!this.delayOver) return false
        if (this.period == 0L) return false
        if (this.counter % this.period == 0L) return true

        return false
    }
}