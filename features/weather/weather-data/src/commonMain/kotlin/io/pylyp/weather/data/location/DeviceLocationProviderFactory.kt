package io.pylyp.weather.data.location

import io.pylyp.weather.domain.location.DeviceLocationProvider

internal expect fun createDeviceLocationProvider(): DeviceLocationProvider
