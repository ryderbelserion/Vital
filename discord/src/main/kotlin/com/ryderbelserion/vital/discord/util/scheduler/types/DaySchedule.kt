package com.ryderbelserion.vital.discord.util.scheduler.types

import com.ryderbelserion.vital.discord.util.scheduler.Schedule
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

public class DaySchedule(
    private val days: Set<DayOfWeek>,
    private val time: LocalTime,
    override val isRepeating: Boolean = true,
    private val block: suspend () -> Unit,
) : Schedule {

    override suspend fun execute() {
        this.block()
    }

    override fun shouldRun(nowDateTime: LocalDateTime): Boolean {
        if (nowDateTime.dayOfWeek !in this.days) return false

        return nowDateTime.toLocalTime() == this.time
    }
}