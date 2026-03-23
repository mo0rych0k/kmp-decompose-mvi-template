package io.pylyp.weather.ui.skytrack.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class CommonWeatherUi(
    val temperatureC: Double?,
    val humidityPercent: Int?,
    val pressureMmHg: Double?,
    val windDirectionDeg: Int?,
    val windDescription: String?,
    val description: String?,
) {
    internal companion object {
        val Empty: CommonWeatherUi = CommonWeatherUi(
            temperatureC = null,
            humidityPercent = null,
            pressureMmHg = null,
            windDirectionDeg = null,
            windDescription = null,
            description = null,
        )
    }
}
