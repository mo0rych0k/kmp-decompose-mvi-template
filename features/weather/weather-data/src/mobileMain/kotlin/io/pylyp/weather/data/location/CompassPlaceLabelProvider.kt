package io.pylyp.weather.data.location

import dev.jordond.compass.Place
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.mobile
import io.pylyp.weather.domain.location.PlaceLabelProvider

/**
 * Reverse geocoding via [Compass](https://compass.jordond.dev/) (platform geocoder on Android / iOS).
 */
internal class CompassPlaceLabelProvider(
    private val geocoder: Geocoder = Geocoder.mobile(),
) : PlaceLabelProvider {

    override suspend fun resolveLabel(latitude: Double, longitude: Double): String? {
        val place = geocoder.reverse(latitude, longitude).getFirstOrNull() ?: return null
        return place.toDisplayLabel()
    }
}

private fun Place.toDisplayLabel(): String? {
    if (isEmpty) return null
    val localityLine = listOfNotNull(locality, administrativeArea)
        .filter { it.isNotBlank() }
        .distinct()
        .joinToString(", ")
        .takeIf { it.isNotBlank() }
    val namePart = name?.takeIf { it.isNotBlank() }
    return localityLine ?: namePart ?: runCatching { firstValue }.getOrNull()
}
