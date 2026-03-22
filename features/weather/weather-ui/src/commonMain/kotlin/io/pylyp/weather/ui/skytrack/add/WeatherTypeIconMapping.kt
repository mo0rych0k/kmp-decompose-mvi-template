package io.pylyp.weather.ui.skytrack.add

import io.pylyp.common.resources.Res
import io.pylyp.common.resources.ic_temperature
import io.pylyp.common.resources.ic_weather_cloudy
import io.pylyp.common.resources.ic_weather_overcast
import io.pylyp.common.resources.ic_weather_rain
import io.pylyp.common.resources.ic_weather_sunny
import io.pylyp.common.resources.ic_wind

import io.pylyp.weather.domain.entity.WeatherTypeDD
import org.jetbrains.compose.resources.DrawableResource

/**
 * Maps [WeatherTypeDD] to drawable resource for icon display.
 */
public fun WeatherTypeDD.toWeatherIconRes(): DrawableResource =
    when (this) {
        WeatherTypeDD.SUNNY -> Res.drawable.ic_weather_sunny
        WeatherTypeDD.CLOUDY -> Res.drawable.ic_weather_cloudy
        WeatherTypeDD.OVERCAST -> Res.drawable.ic_weather_overcast
        WeatherTypeDD.RAIN -> Res.drawable.ic_weather_rain
    }

internal val temperatureIconRes: DrawableResource get() = Res.drawable.ic_temperature

internal val windIconRes: DrawableResource get() = Res.drawable.ic_wind

internal val weatherSectionIconRes: DrawableResource get() = Res.drawable.ic_weather_cloudy
