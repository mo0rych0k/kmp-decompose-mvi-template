package io.pylyp.weather.ui.skytrack.model

import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.domain.entity.WindDirectionDD
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt

internal fun WeatherTypeDD.toWeatherTypeUi(): WeatherTypeUi =
    when (this) {
        WeatherTypeDD.SUNNY -> WeatherTypeUi.SUNNY
        WeatherTypeDD.CLOUDY -> WeatherTypeUi.CLOUDY
        WeatherTypeDD.OVERCAST -> WeatherTypeUi.OVERCAST
        WeatherTypeDD.RAIN -> WeatherTypeUi.RAIN
        WeatherTypeDD.SNOW -> WeatherTypeUi.SNOW
        WeatherTypeDD.FOG -> WeatherTypeUi.FOG
        WeatherTypeDD.LIGHTNING -> WeatherTypeUi.LIGHTNING
        WeatherTypeDD.HAIL -> WeatherTypeUi.HAIL
    }

internal fun WeatherTypeUi.toWeatherTypeDD(): WeatherTypeDD =
    when (this) {
        WeatherTypeUi.SUNNY -> WeatherTypeDD.SUNNY
        WeatherTypeUi.CLOUDY -> WeatherTypeDD.CLOUDY
        WeatherTypeUi.OVERCAST -> WeatherTypeDD.OVERCAST
        WeatherTypeUi.RAIN -> WeatherTypeDD.RAIN
        WeatherTypeUi.SNOW -> WeatherTypeDD.SNOW
        WeatherTypeUi.FOG -> WeatherTypeDD.FOG
        WeatherTypeUi.LIGHTNING -> WeatherTypeDD.LIGHTNING
        WeatherTypeUi.HAIL -> WeatherTypeDD.HAIL
    }

internal fun WindDirectionDD.toWindDirectionUi(): WindDirectionUi =
    when (this) {
        WindDirectionDD.NORTH -> WindDirectionUi.NORTH
        WindDirectionDD.NORTH_EAST -> WindDirectionUi.NORTH_EAST
        WindDirectionDD.EAST -> WindDirectionUi.EAST
        WindDirectionDD.SOUTH_EAST -> WindDirectionUi.SOUTH_EAST
        WindDirectionDD.SOUTH -> WindDirectionUi.SOUTH
        WindDirectionDD.SOUTH_WEST -> WindDirectionUi.SOUTH_WEST
        WindDirectionDD.WEST -> WindDirectionUi.WEST
        WindDirectionDD.NORTH_WEST -> WindDirectionUi.NORTH_WEST
    }

/** Maps UI wind direction to domain (save / use-cases). */
internal fun WindDirectionUi.toWindDirectionDomain(): WindDirectionDD =
    when (this) {
        WindDirectionUi.NORTH -> WindDirectionDD.NORTH
        WindDirectionUi.NORTH_EAST -> WindDirectionDD.NORTH_EAST
        WindDirectionUi.EAST -> WindDirectionDD.EAST
        WindDirectionUi.SOUTH_EAST -> WindDirectionDD.SOUTH_EAST
        WindDirectionUi.SOUTH -> WindDirectionDD.SOUTH
        WindDirectionUi.SOUTH_WEST -> WindDirectionDD.SOUTH_WEST
        WindDirectionUi.WEST -> WindDirectionDD.WEST
        WindDirectionUi.NORTH_WEST -> WindDirectionDD.NORTH_WEST
    }

/** Meteorological bearing to 8-point sector centre: 0° = north, 45° per step clockwise. */
internal fun WindDirectionUi.toCenterBearingDegrees(): Float = ordinal * 45f

internal fun CommonWeatherDD.toCommonWeatherUi(): CommonWeatherUi =
    CommonWeatherUi(
        temperatureC = temperatureC,
        humidityPercent = humidityPercent,
        pressureMmHg = pressureMmHg,
        windDirectionDeg = windDirectionDeg,
        windDescription = windDescription,
        description = description,
    )

internal fun CommonWeatherUi.toCommonWeatherDD(): CommonWeatherDD =
    CommonWeatherDD(
        temperatureC = temperatureC,
        humidityPercent = humidityPercent,
        pressureMmHg = pressureMmHg,
        windDirectionDeg = windDirectionDeg,
        windDescription = windDescription,
        description = description,
    )

internal fun GeoCoordinatesDD.toGeoCoordinatesUi(): GeoCoordinatesUi =
    GeoCoordinatesUi(latitude = latitude, longitude = longitude)

internal fun GeoCoordinatesUi.toGeoCoordinatesDD(): GeoCoordinatesDD =
    GeoCoordinatesDD(latitude = latitude, longitude = longitude)

internal fun WeatherObservationRecordDD.toWeatherObservationRecordUi(): WeatherObservationRecordUi =
    WeatherObservationRecordUi(
        id = id,
        createdAtEpochMs = createdAtEpochMs,
        location = toObservationLocationUi(),
        userTemperatureC = userTemperatureC,
        userHumidityPercent = userHumidityPercent,
        userPressureMmHg = userPressureMmHg,
        userWindDirection = userWindDirection.toWindDirectionUi(),
        userWindStrengthPercent = userWindStrengthPercent,
        userWeatherTypes = userWeatherTypes.map { it.toWeatherTypeUi() }.toSet(),
        apiTemperatureC = apiTemperatureC,
        apiHumidityPercent = apiHumidityPercent,
        apiPressureMmHg = apiPressureMmHg,
        apiWindDescription = apiWindDescription,
        apiDescription = apiDescription,
        temperatureDeltaC = temperatureDeltaC,
        isHighDiscrepancy = isHighDiscrepancy,
    )

internal fun WeatherObservationRecordDD.toObservationLocationUi(): ObservationLocationUi {
    val label = locationLabel?.takeIf { it.isNotBlank() }
    val coords = formatCoordinatesDisplay(latitude, longitude)
    return when {
        label != null -> ObservationLocationUi.WithPlace(placeLabel = label, coordinatesLine = coords)
        latitude != null -> ObservationLocationUi.CoordinatesOnly(coordinatesLine = coords)
        else -> ObservationLocationUi.Unknown
    }
}

/** ~100 m precision for display (two decimals). */
internal fun formatCoordinatesDisplay(latitude: Double?, longitude: Double?): String {
    if (latitude == null || longitude == null) return "—"
    return "${formatCoord(latitude)}°, ${formatCoord(longitude)}°"
}

private fun formatCoord(value: Double): String {
    val sign = if (value < 0) "-" else ""
    val abs = abs(value)
    val rounded = round(abs * 100.0) / 100.0
    val intPart = rounded.toInt()
    val frac = ((rounded - intPart) * 100).roundToInt().toString().padStart(2, '0')
    return "$sign$intPart.$frac"
}
