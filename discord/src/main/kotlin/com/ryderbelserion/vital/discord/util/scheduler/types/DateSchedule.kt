package com.ryderbelserion.vital.discord.util.scheduler.types

import com.ryderbelserion.vital.discord.util.scheduler.Schedule
import java.time.LocalDateTime

/**
 * A schedule that is based on a date.
 */
public class DateSchedule(
    private val dateTime: LocalDateTime,
    private val block: suspend () -> Unit,
) : Schedule {

    init {
        if (this.dateTime.isBefore(LocalDateTime.now())) throw IllegalArgumentException("DateTime must be after now")
    }

    override val isRepeating: Boolean = false

    override suspend fun execute() {
        this.block()
    }

    override fun shouldRun(nowDateTime: LocalDateTime): Boolean {
        return nowDateTime.isEqual(this.dateTime)
    }
}