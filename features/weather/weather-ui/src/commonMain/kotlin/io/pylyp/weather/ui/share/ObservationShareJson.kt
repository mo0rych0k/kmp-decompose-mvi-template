package io.pylyp.weather.ui.share

import io.pylyp.weather.ui.skytrack.model.ObservationLocationUi
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal data class ObservationShareData(
    val id: Long,
    val createdAtEpochMs: Long,
    val location: LocationShareData,
    val userTemperatureC: Double,
    val userHumidityPercent: Int,
    val userPressureMmHg: Int,
    val userWindDirection: String,
    val userWindStrengthPercent: Int,
    val userWeatherTypes: List<String>,
    val apiTemperatureC: Double?,
    val apiHumidityPercent: Int?,
    val apiPressureMmHg: Double?,
    val apiWindDescription: String?,
    val apiDescription: String?,
    val temperatureDeltaC: Double,
    val isHighDiscrepancy: Boolean,
)

@Serializable
internal sealed class LocationShareData {
    @Serializable
    data class WithPlace(val placeLabel: String, val coordinatesLine: String) : LocationShareData()

    @Serializable
    data class CoordinatesOnly(val coordinatesLine: String) : LocationShareData()

    @Serializable
    data object Unknown : LocationShareData()
}

private fun WeatherObservationRecordUi.toObservationShareData(): ObservationShareData =
    ObservationShareData(
        id = id,
        createdAtEpochMs = createdAtEpochMs,
        location = when (val loc = location) {
            is ObservationLocationUi.WithPlace ->
                LocationShareData.WithPlace(
                    placeLabel = loc.placeLabel,
                    coordinatesLine = loc.coordinatesLine,
                )

            is ObservationLocationUi.CoordinatesOnly ->
                LocationShareData.CoordinatesOnly(coordinatesLine = loc.coordinatesLine)

            ObservationLocationUi.Unknown -> LocationShareData.Unknown
        },
        userTemperatureC = userTemperatureC,
        userHumidityPercent = userHumidityPercent,
        userPressureMmHg = userPressureMmHg,
        userWindDirection = userWindDirection.name,
        userWindStrengthPercent = userWindStrengthPercent,
        userWeatherTypes = userWeatherTypes.map { it.name },
        apiTemperatureC = apiTemperatureC,
        apiHumidityPercent = apiHumidityPercent,
        apiPressureMmHg = apiPressureMmHg,
        apiWindDescription = apiWindDescription,
        apiDescription = apiDescription,
        temperatureDeltaC = temperatureDeltaC,
        isHighDiscrepancy = isHighDiscrepancy,
    )

internal fun WeatherObservationRecordUi.toShareJson(): String =
    Json { prettyPrint = true }.encodeToString(toObservationShareData())

internal fun List<WeatherObservationRecordUi>.toShareJsonArray(): String =
    Json { prettyPrint = true }.encodeToString(map { it.toObservationShareData() })
