package io.pylyp.weather.data.location

import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.roundedForStorage
import io.pylyp.weather.domain.location.DeviceLocationProvider

internal actual fun createDeviceLocationProvider(): DeviceLocationProvider {
    return CompassDeviceLocationProvider(geolocator = Geolocator.mobile())
}

/**
 * Uses [Compass](https://compass.jordond.dev/) for GPS with permission handling on Android / iOS.
 */
internal class CompassDeviceLocationProvider(
    private val geolocator: Geolocator,
) : DeviceLocationProvider {

    override suspend fun getCurrentLocation(): GeoCoordinatesDD? {
        return geolocator.current().getOrNull()?.let { loc ->
            val coords = loc.coordinates
            GeoCoordinatesDD(latitude = coords.latitude, longitude = coords.longitude).roundedForStorage()
        }
    }
}
