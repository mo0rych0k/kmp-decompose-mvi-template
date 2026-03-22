package io.pylyp.weather.data.location

import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.roundedForStorage
import io.pylyp.weather.domain.location.DeviceLocationProvider

private const val KYIV_LAT = 50.4501
private const val KYIV_LON = 30.5234

internal class FallbackDeviceLocationProvider : DeviceLocationProvider {
    override suspend fun getCurrentLocation(): GeoCoordinatesDD {
        return GeoCoordinatesDD(latitude = KYIV_LAT, longitude = KYIV_LON).roundedForStorage()
    }
}
