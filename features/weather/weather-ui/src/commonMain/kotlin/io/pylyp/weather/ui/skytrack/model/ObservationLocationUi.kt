package io.pylyp.weather.ui.skytrack.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed class ObservationLocationUi {
    data class WithPlace(
        val placeLabel: String,
        val coordinatesLine: String,
    ) : ObservationLocationUi()

    data class CoordinatesOnly(
        val coordinatesLine: String,
    ) : ObservationLocationUi()

    data object Unknown : ObservationLocationUi()
}
