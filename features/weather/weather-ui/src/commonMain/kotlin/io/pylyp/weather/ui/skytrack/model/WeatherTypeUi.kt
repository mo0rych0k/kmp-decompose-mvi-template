package io.pylyp.weather.ui.skytrack.model

import androidx.compose.runtime.Immutable

@Immutable
internal enum class WeatherTypeUi {
    SUNNY,
    CLOUDY,
    OVERCAST,
    RAIN,
    SNOW,
    FOG,
    LIGHTNING,
    HAIL,
}
