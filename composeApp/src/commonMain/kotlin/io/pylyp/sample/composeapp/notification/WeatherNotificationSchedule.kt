@file:OptIn(kotlin.time.ExperimentalTime::class)

package io.pylyp.sample.composeapp.notification

import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Next local-time delivery in [10:00, 18:00) with a random minute inside that window.
 * If that time has already passed today, schedules for the same clock time tomorrow.
 */
public fun computeNextWeatherNotificationInstant(
    now: Instant = Clock.System.now(),
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    random: Random = Random.Default,
): Instant {
    val nowEpochMs = now.toEpochMilliseconds()
    val localNow = Instant.fromEpochMilliseconds(nowEpochMs).toLocalDateTime(timeZone)
    val today = localNow.date
    val randomMinuteOfDayInclusive = random.nextInt(from = 10 * 60, until = 18 * 60)
    val hour = randomMinuteOfDayInclusive / 60
    val minute = randomMinuteOfDayInclusive % 60
    val candidateLocal = LocalDateTime(today, LocalTime(hour, minute))
    var candidateEpochMs = candidateLocal.toInstant(timeZone).toEpochMilliseconds()
    if (candidateEpochMs <= nowEpochMs) {
        val tomorrow = today.plus(DatePeriod(days = 1))
        val nextLocal = LocalDateTime(tomorrow, LocalTime(hour, minute))
        candidateEpochMs = nextLocal.toInstant(timeZone).toEpochMilliseconds()
    }
    return Instant.fromEpochMilliseconds(candidateEpochMs)
}

public fun millisUntil(
    target: Instant,
    now: Instant = Clock.System.now(),
): Long =
    (target - now).inWholeMilliseconds.coerceAtLeast(0L)
