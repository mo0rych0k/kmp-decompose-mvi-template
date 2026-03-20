package io.pylyp.weather.data

import io.pylyp.weather.data.mappers.toDomain
import io.pylyp.weather.data.network.MetNorwayRemoteDataSource
import io.pylyp.weather.data.network.OpenMeteoRemoteDataSource
import io.pylyp.weather.data.network.WttrInRemoteDataSource
import io.pylyp.weather.domain.entity.CurrentWeatherDD
import io.pylyp.weather.domain.entity.WeatherServiceType
import io.pylyp.weather.domain.repository.WeatherRepository

internal class WeatherRepositoryImpl(
    private val openMeteoRemoteDataSource: OpenMeteoRemoteDataSource,
    private val wttrInRemoteDataSource: WttrInRemoteDataSource,
    private val metNorwayRemoteDataSource: MetNorwayRemoteDataSource,
) : WeatherRepository {

    override suspend fun getCurrentWeatherKyiv(service: WeatherServiceType): CurrentWeatherDD {
        return when (service) {
            WeatherServiceType.OPEN_METEO ->
                openMeteoRemoteDataSource.getKyivCurrentWeather().toDomain()

            WeatherServiceType.WTTR_IN ->
                wttrInRemoteDataSource.getKyivJson().toDomain()

            WeatherServiceType.MET_NORWAY ->
                metNorwayRemoteDataSource.getKyivCompactForecast().toDomain()
        }
    }
}
