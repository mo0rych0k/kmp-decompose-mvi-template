package io.pylyp.weather.data.location

import io.pylyp.weather.domain.location.PlaceLabelProvider

internal actual fun createPlaceLabelProvider(): PlaceLabelProvider {
    return CompassPlaceLabelProvider()
}
