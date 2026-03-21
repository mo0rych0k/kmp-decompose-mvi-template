package io.pylyp.weather.domain.repository

import io.pylyp.weather.domain.entity.CommonWeatherDD

public interface WeatherRepository {
    /** Reference current weather at coordinates (SkyTrack verification). */
    public suspend fun getOpenWeatherCurrentWeather(latitude: Double, longitude: Double): CommonWeatherDD
}
