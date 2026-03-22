package io.pylyp.weather.ui.skytrack.model

import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
internal fun todayObservationCalendarDayUi(): ObservationCalendarDayUi {
    val epochMs = Clock.System.now().toEpochMilliseconds()
    val instant = Instant.fromEpochMilliseconds(epochMs)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return ObservationCalendarDayUi(
        year = date.year,
        monthNumber = date.month.number,
        dayOfMonth = date.day,
    )
}

internal fun ObservationCalendarDayUi.toLocalDate(): LocalDate =
    LocalDate(year = year, month = monthNumber, day = dayOfMonth)

internal fun dayEpochMillisRangeExclusive(day: ObservationCalendarDayUi): LongRange {
    val tz = TimeZone.currentSystemDefault()
    val start = day.toLocalDate().atStartOfDayIn(tz)
    val end = start.plus(1, DateTimeUnit.DAY, tz)
    return start.toEpochMilliseconds() until end.toEpochMilliseconds()
}

internal fun WeatherObservationRecordDD.isInLocalDay(day: ObservationCalendarDayUi): Boolean {
    val range = dayEpochMillisRangeExclusive(day)
    return createdAtEpochMs in range
}

internal fun countObservationsByDayOfMonth(
    records: List<WeatherObservationRecordDD>,
    year: Int,
    monthNumber: Int,
): Map<Int, Int> {
    val tz = TimeZone.currentSystemDefault()
    val counts = mutableMapOf<Int, Int>()
    for (record in records) {
        val localDate = Instant.fromEpochMilliseconds(record.createdAtEpochMs).toLocalDateTime(tz).date
        if (localDate.year == year && localDate.month.number == monthNumber) {
            val d = localDate.day
            counts[d] = counts.getOrElse(d) { 0 } + 1
        }
    }
    return counts
}

internal fun lastDayOfMonth(year: Int, monthNumber: Int): Int {
    val first = LocalDate(year, monthNumber, 1)
    val nextMonthFirst = first.plus(1, DateTimeUnit.MONTH)
    return nextMonthFirst.minus(1, DateTimeUnit.DAY).day
}

internal fun formatObservationDayIso(day: ObservationCalendarDayUi): String = day.toLocalDate().toString()

internal fun shiftMonth(year: Int, monthNumber: Int, deltaMonths: Int): Pair<Int, Int> {
    val base = LocalDate(year, monthNumber, 1).plus(deltaMonths, DateTimeUnit.MONTH)
    return base.year to base.month.number
}
