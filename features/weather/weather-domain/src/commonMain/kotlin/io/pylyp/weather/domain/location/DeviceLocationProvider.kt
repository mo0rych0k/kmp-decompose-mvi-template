package io.pylyp.weather.domain.location

import io.pylyp.weather.domain.entity.GeoCoordinatesDD

public interface DeviceLocationProvider {
    public suspend fun getCurrentLocation(): GeoCoordinatesDD?
}
