package io.pylyp.weather.data

import io.pylyp.weather.data.mappers.toCommonWeather
import io.pylyp.weather.data.network.OpenWeatherRemoteDataSource
import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.repository.WeatherRepository

internal class WeatherRepositoryImpl(
    private val openWeatherRemoteDataSource: OpenWeatherRemoteDataSource,
) : WeatherRepository {

    override suspend fun getOpenWeatherCurrentWeather(latitude: Double, longitude: Double): CommonWeatherDD =
        openWeatherRemoteDataSource.getCurrentWeather(latitude, longitude).toCommonWeather()
}
