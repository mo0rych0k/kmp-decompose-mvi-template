package io.pylyp.weather.ui.skytrack.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class WeatherObservationRecordUi(
    val id: Long,
    val createdAtEpochMs: Long,
    val location: ObservationLocationUi,
    val userTemperatureC: Double,
    val userHumidityPercent: Int,
    val userPressureMmHg: Int,
    val userWindDirection: WindDirectionUi,
    val userWindStrengthPercent: Int,
    val userWeatherTypes: Set<WeatherTypeUi>,
    val apiTemperatureC: Double?,
    val apiHumidityPercent: Int?,
    val apiPressureMmHg: Double?,
    val apiWindDescription: String?,
    val apiDescription: String?,
    val temperatureDeltaC: Double,
    val isHighDiscrepancy: Boolean,
)
