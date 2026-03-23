package io.pylyp.weather.ui.skytrack.calendar.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.model.ObservationCalendarDayUi
import io.pylyp.weather.ui.skytrack.model.isAfter
import io.pylyp.weather.ui.skytrack.model.isBefore
import io.pylyp.weather.ui.skytrack.model.isSameDayAs
import io.pylyp.weather.ui.skytrack.model.lastDayOfMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

@Immutable
internal data class ObservationCalendarGridUi(
    val year: Int,
    val monthNumber: Int,
    val countsByDay: Map<Int, Int>,
    val focusDay: ObservationCalendarDayUi,
    val today: ObservationCalendarDayUi,
)

@Composable
internal fun ObservationCalendarGridComponent(
    monthTitle: String,
    monthSubtitle: String,
    grid: ObservationCalendarGridUi,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDayClick: (Int) -> Unit,
    onEmptyPastDateTapped: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val first = LocalDate(grid.year, grid.monthNumber, 1)
    val firstDayOffset = (first.dayOfWeek.ordinal - DayOfWeek.MONDAY.ordinal + 7) % 7
    val daysInMonth = lastDayOfMonth(grid.year, grid.monthNumber)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = monthTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = monthSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onNextMonth) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            weekdayLabels.forEach { label ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        val totalCells = firstDayOffset + daysInMonth
        val rowCount: Int = (totalCells + 6) / 7
        for (row in 0 until rowCount) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    val dayNumber = index - firstDayOffset + 1
                    if (dayNumber < 1 || dayNumber > daysInMonth) {
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val count = grid.countsByDay[dayNumber] ?: 0
                        val cellDay = ObservationCalendarDayUi(
                            year = grid.year,
                            monthNumber = grid.monthNumber,
                            dayOfMonth = dayNumber,
                        )
                        val isHighlighted =
                            grid.focusDay.year == grid.year &&
                                grid.focusDay.monthNumber == grid.monthNumber &&
                                grid.focusDay.dayOfMonth == dayNumber
                        val isToday = cellDay.isSameDayAs(grid.today)
                        val isFuture = cellDay.isAfter(grid.today)
                        val isPast = cellDay.isBefore(grid.today)
                        val isEmptyPastDate = isPast && count == 0
                        DayCell(
                            modifier = Modifier.weight(1f),
                            dayOfMonth = dayNumber,
                            count = count,
                            isHighlighted = isHighlighted,
                            isToday = isToday,
                            isFuture = isFuture,
                            isEmptyPastDate = isEmptyPastDate,
                            onClick = {
                                when {
                                    isFuture -> {}
                                    isEmptyPastDate -> onEmptyPastDateTapped.invoke()
                                    else -> {
                                        onDayClick(dayNumber)
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

private val weekdayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
private fun DayCell(
    dayOfMonth: Int,
    count: Int,
    isHighlighted: Boolean,
    isToday: Boolean,
    isFuture: Boolean,
    isEmptyPastDate: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(10.dp)
    val bg =
        when {
            isFuture -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
            isEmptyPastDate -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
            isHighlighted -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        }
    val todayBorder =
        if (isToday) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape)
            .clickable(enabled = !isFuture, onClick = onClick),
        color = bg,
        shape = shape,
        border = todayBorder,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = dayOfMonth.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                )
                if (count > 0) {
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun ObservationCalendarGridComponentPreview() {
    AppTheme {
        ObservationCalendarGridComponent(
            monthTitle = "March 2025",
            monthSubtitle = "Observation counts",
            grid = ObservationCalendarGridUi(
                year = 2025,
                monthNumber = 3,
                countsByDay = mapOf(1 to 2, 15 to 5, 22 to 1),
                focusDay = ObservationCalendarDayUi(2025, 3, 22),
                today = ObservationCalendarDayUi(2025, 3, 23),
            ),
            onPreviousMonth = {},
            onNextMonth = {},
            onDayClick = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
        )
    }
}
