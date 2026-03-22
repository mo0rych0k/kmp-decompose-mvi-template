package io.pylyp.weather.ui.skytrack.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class GeoCoordinatesUi(
    val latitude: Double,
    val longitude: Double,
)
