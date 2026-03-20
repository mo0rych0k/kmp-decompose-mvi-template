package io.pylyp.weather.domain.usecase

import io.pylyp.common.core.foundation.entity.SuspendUseCase
import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.SkyTrackBackgroundWeather
import io.pylyp.weather.domain.entity.roundedForStorage
import io.pylyp.weather.domain.location.DeviceLocationProvider
import io.pylyp.weather.domain.repository.WeatherRepository

private const val KYIV_LAT = 50.4501
private const val KYIV_LON = 30.5234

public class LoadSkyTrackBackgroundWeatherUseCase(
    private val locationProvider: DeviceLocationProvider,
    private val weatherRepository: WeatherRepository,
) : SuspendUseCase<Unit, SkyTrackBackgroundWeather>() {
    override suspend fun execute(parameters: Unit): SkyTrackBackgroundWeather {
        val coords = locationProvider.getCurrentLocation()
            ?.roundedForStorage()
            ?: GeoCoordinatesDD(latitude = KYIV_LAT, longitude = KYIV_LON).roundedForStorage()
        val api = weatherRepository.getOpenWeatherCurrentWeather(
            latitude = coords.latitude,
            longitude = coords.longitude,
        )
        return SkyTrackBackgroundWeather(coordinates = coords, apiWeather = api)
    }
}
