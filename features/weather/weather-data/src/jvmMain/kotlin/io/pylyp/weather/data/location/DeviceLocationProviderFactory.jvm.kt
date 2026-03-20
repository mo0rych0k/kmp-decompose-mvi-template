package io.pylyp.weather.data.location

import io.pylyp.weather.domain.location.DeviceLocationProvider

internal actual fun createDeviceLocationProvider(): DeviceLocationProvider {
    return FallbackDeviceLocationProvider()
}
