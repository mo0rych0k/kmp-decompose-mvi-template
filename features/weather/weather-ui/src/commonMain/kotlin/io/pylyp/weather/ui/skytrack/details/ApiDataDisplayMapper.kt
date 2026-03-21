package io.pylyp.weather.ui.skytrack.details

import io.pylyp.weather.ui.skytrack.model.WeatherTypeUi
import io.pylyp.weather.ui.skytrack.model.WindDirectionUi

/**
 * Maps API service data (description text, wind string) to local-like display format:
 * cloudiness, precipitation, wind direction and strength.
 */
internal data class ApiDataDisplayUi(
    val cloudinessTypes: Set<WeatherTypeUi>,
    val precipitationTypes: Set<WeatherTypeUi>,
    val windDirection: WindDirectionUi?,
    val windStrengthPercent: Int,
    val windSpeedKmh: Int?,
)

/**
 * Maps apiDescription (e.g. "Clear sky", "Rain") and apiWindDescription (e.g. "N 12 km/h")
 * to display-ready types matching local observation format.
 */
internal fun mapApiDataToDisplay(
    apiDescription: String?,
    apiWindDescription: String?,
): ApiDataDisplayUi {
    val desc = apiDescription?.trim()?.lowercase() ?: ""
    val cloudiness = descToCloudiness(desc)
    val precipitation = descToPrecipitation(desc)
    val (windDir, windStrength, windSpeed) = parseWindDescription(apiWindDescription)
    return ApiDataDisplayUi(
        cloudinessTypes = cloudiness,
        precipitationTypes = precipitation,
        windDirection = windDir,
        windStrengthPercent = windStrength,
        windSpeedKmh = windSpeed,
    )
}

private fun descToCloudiness(desc: String): Set<WeatherTypeUi> =
    when {
        desc.contains("clear") && !desc.contains("partly") -> setOf(WeatherTypeUi.SUNNY)
        desc.contains("partly") || desc.contains("mainly clear") -> setOf(WeatherTypeUi.CLOUDY)
        desc.contains("overcast") -> setOf(WeatherTypeUi.OVERCAST)
        desc.isEmpty() -> emptySet()
        else -> setOf(WeatherTypeUi.CLOUDY)
    }

private fun descToPrecipitation(desc: String): Set<WeatherTypeUi> {
    val set = mutableSetOf<WeatherTypeUi>()
    if (desc.contains("rain") || desc.contains("drizzle") || desc.contains("shower")) set.add(WeatherTypeUi.RAIN)
    if (desc.contains("snow")) set.add(WeatherTypeUi.SNOW)
    if (desc.contains("fog")) set.add(WeatherTypeUi.FOG)
    if (desc.contains("thunder") || desc.contains("lightning")) set.add(WeatherTypeUi.LIGHTNING)
    if (desc.contains("hail")) set.add(WeatherTypeUi.HAIL)
    return set
}

private fun parseWindDescription(windDesc: String?): Triple<WindDirectionUi?, Int, Int?> {
    val s = windDesc?.trim() ?: return Triple(null, 0, null)
    val parts = s.split("\\s+".toRegex())
    val dirStr = parts.firstOrNull()?.uppercase()
    val dir = dirStr?.let { compassToWindDirection(it) }
    val speed = parts.getOrNull(1)?.replace(Regex("[^0-9]"), "")?.toIntOrNull()
    val strength = when {
        speed == null -> 0
        speed <= 5 -> 15
        speed <= 15 -> 35
        speed <= 30 -> 60
        speed <= 50 -> 80
        else -> 95
    }
    return Triple(dir, strength, speed)
}

private fun compassToWindDirection(compass: String): WindDirectionUi? =
    when (compass) {
        "N" -> WindDirectionUi.NORTH
        "NE" -> WindDirectionUi.NORTH_EAST
        "E" -> WindDirectionUi.EAST
        "SE" -> WindDirectionUi.SOUTH_EAST
        "S" -> WindDirectionUi.SOUTH
        "SW" -> WindDirectionUi.SOUTH_WEST
        "W" -> WindDirectionUi.WEST
        "NW" -> WindDirectionUi.NORTH_WEST
        else -> null
    }
