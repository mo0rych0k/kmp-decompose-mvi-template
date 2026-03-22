package io.pylyp.weather.ui.skytrack.add

import io.pylyp.common.resources.Res
import io.pylyp.common.resources.ic_temperature
import io.pylyp.common.resources.ic_weather_cloudy
import io.pylyp.common.resources.ic_weather_fog
import io.pylyp.common.resources.ic_weather_hail
import io.pylyp.common.resources.ic_weather_lightning
import io.pylyp.common.resources.ic_weather_overcast
import io.pylyp.common.resources.ic_weather_rain
import io.pylyp.common.resources.ic_weather_snow
import io.pylyp.common.resources.ic_weather_sunny
import io.pylyp.common.resources.ic_wind
import io.pylyp.common.resources.ic_wind_windy

import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.ui.skytrack.model.WeatherTypeUi
import org.jetbrains.compose.resources.DrawableResource

/**
 * Maps [WeatherTypeDD] to drawable resource for icon display (domain → resources; use [WeatherTypeUi.toWeatherIconRes] from UI).
 */
public fun WeatherTypeDD.toWeatherIconRes(): DrawableResource =
    when (this) {
        WeatherTypeDD.SUNNY -> Res.drawable.ic_weather_sunny
        WeatherTypeDD.CLOUDY -> Res.drawable.ic_weather_cloudy
        WeatherTypeDD.OVERCAST -> Res.drawable.ic_weather_overcast
        WeatherTypeDD.RAIN -> Res.drawable.ic_weather_rain
        WeatherTypeDD.SNOW -> Res.drawable.ic_weather_snow
        WeatherTypeDD.FOG -> Res.drawable.ic_weather_fog
        WeatherTypeDD.LIGHTNING -> Res.drawable.ic_weather_lightning
        WeatherTypeDD.HAIL -> Res.drawable.ic_weather_hail
    }

internal fun WeatherTypeUi.toWeatherIconRes(): DrawableResource =
    when (this) {
        WeatherTypeUi.SUNNY -> Res.drawable.ic_weather_sunny
        WeatherTypeUi.CLOUDY -> Res.drawable.ic_weather_cloudy
        WeatherTypeUi.OVERCAST -> Res.drawable.ic_weather_overcast
        WeatherTypeUi.RAIN -> Res.drawable.ic_weather_rain
        WeatherTypeUi.SNOW -> Res.drawable.ic_weather_snow
        WeatherTypeUi.FOG -> Res.drawable.ic_weather_fog
        WeatherTypeUi.LIGHTNING -> Res.drawable.ic_weather_lightning
        WeatherTypeUi.HAIL -> Res.drawable.ic_weather_hail
    }

internal val temperatureIconRes: DrawableResource get() = Res.drawable.ic_temperature

internal val windIconRes: DrawableResource get() = Res.drawable.ic_wind

/** Generic wind icon (forecast-blow style) for section titles and headers. */
internal val windSectionIconRes: DrawableResource get() = Res.drawable.ic_wind_windy

internal val weatherSectionIconRes: DrawableResource get() = Res.drawable.ic_weather_cloudy

internal val precipitationSectionIconRes: DrawableResource get() = Res.drawable.ic_weather_rain

internal val cloudinessTypes: List<WeatherTypeUi> = listOf(
    WeatherTypeUi.SUNNY,
    WeatherTypeUi.CLOUDY,
    WeatherTypeUi.OVERCAST,
)

internal val precipitationTypes: List<WeatherTypeUi> = listOf(
    WeatherTypeUi.RAIN,
    WeatherTypeUi.SNOW,
    WeatherTypeUi.FOG,
    WeatherTypeUi.LIGHTNING,
    WeatherTypeUi.HAIL,
)
