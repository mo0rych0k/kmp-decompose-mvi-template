package io.pylyp.weather.domain.repository

import io.pylyp.weather.domain.entity.CommonWeatherDD

public interface WeatherRepository {
    /** Reference current weather at coordinates via Open-Meteo (SkyTrack verification). */
    public suspend fun getOpenMeteoCurrentWeather(latitude: Double, longitude: Double): CommonWeatherDD
}
