package com.ryderbelserion.vital.discord.util.scheduler

import com.ryderbelserion.vital.discord.util.scheduler.types.DateSchedule
import com.ryderbelserion.vital.discord.util.scheduler.types.DaySchedule
import kotlinx.coroutines.*
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public object Scheduler : CoroutineScope {

    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val job = Job()

    override val coroutineContext: CoroutineContext get() = job + dispatcher

    private val schedules = mutableListOf<Schedule>()
    private var clock = Clock.systemDefaultZone()
    private var isStarted = false

    public fun interval(period: Duration, delay: Duration = 0.seconds, task: suspend () -> Unit) {
        schedules.add(ScheduleTimer(period.inWholeSeconds, delay.inWholeSeconds, true, task))
    }

    public fun interval(days: Set<DayOfWeek>, at: LocalTime, task: suspend () -> Unit) {
        schedules.add(DaySchedule(days, at, true, task))
    }

    public fun at(date: LocalDateTime, task: suspend () -> Unit) {
        schedules.add(DateSchedule(date, task))
    }

    public fun countdown(time: Duration, task: suspend () -> Unit) {
        schedules.add(ScheduleTimer(0, time.inWholeSeconds, false, task))
    }

    /**
     * Start the timer logic.
     */
    public fun start(): Scheduler {
        launch {
            var lastCheck: LocalDateTime? = null

            while (true) {
                val second = LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS)

                if (lastCheck != second) {
                    lastCheck = second

                    coroutineScope {
                        launchSchedules(second)
                    }
                }

                delay(50)
            }
        }

        isStarted = true

        return this
    }

    /**
     * Launches each schedule.
     */
    private fun launchSchedules(nowMinute: LocalDateTime) {
        schedules.forEach {
            launch {
                if (!it.shouldRun(nowMinute)) return@launch

                it.execute()

                if (!it.isRepeating) schedules.remove(it)
            }
        }
    }
}

public interface Schedule {
    public val isRepeating: Boolean

    public suspend fun execute()

    public fun shouldRun(nowDateTime: LocalDateTime): Boolean
}