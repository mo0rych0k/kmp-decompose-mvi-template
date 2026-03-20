package io.pylyp.weather.domain.repository

import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.entity.CurrentWeatherDD
import io.pylyp.weather.domain.entity.WeatherServiceType

public interface WeatherRepository {
    public suspend fun getCurrentWeatherKyiv(service: WeatherServiceType): CurrentWeatherDD

    /** OpenWeather current weather at coordinates (SkyTrack verification). */
    public suspend fun getOpenWeatherCurrentWeather(latitude: Double, longitude: Double): CommonWeatherDD
}
