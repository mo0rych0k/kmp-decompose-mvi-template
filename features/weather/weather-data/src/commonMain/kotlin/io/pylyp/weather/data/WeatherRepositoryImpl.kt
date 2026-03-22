package io.pylyp.weather.data

import io.pylyp.weather.data.mappers.toCommonWeather
import io.pylyp.weather.data.network.OpenMeteoRemoteDataSource
import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.repository.WeatherRepository

internal class WeatherRepositoryImpl(
    private val openMeteoRemoteDataSource: OpenMeteoRemoteDataSource,
) : WeatherRepository {

    override suspend fun getOpenMeteoCurrentWeather(latitude: Double, longitude: Double): CommonWeatherDD =
        openMeteoRemoteDataSource.getCurrentWeather(latitude, longitude).toCommonWeather()
}
