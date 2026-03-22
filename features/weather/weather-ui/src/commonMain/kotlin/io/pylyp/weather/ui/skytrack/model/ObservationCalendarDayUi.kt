package io.pylyp.weather.ui.skytrack.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal data class ObservationCalendarDayUi(
    val year: Int,
    val monthNumber: Int,
    val dayOfMonth: Int,
)
