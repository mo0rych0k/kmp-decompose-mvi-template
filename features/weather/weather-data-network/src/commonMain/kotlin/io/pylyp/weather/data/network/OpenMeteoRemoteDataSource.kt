package io.pylyp.weather.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.pylyp.core.threading.DispatcherProvider
import io.pylyp.weather.data.network.entity.OpenMeteoForecastResponseND
import kotlinx.coroutines.withContext

public interface OpenMeteoRemoteDataSource {
    public suspend fun getKyivCurrentWeather(): OpenMeteoForecastResponseND
}

internal class OpenMeteoRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val dispatcherProvider: DispatcherProvider,
) : OpenMeteoRemoteDataSource {

    override suspend fun getKyivCurrentWeather(): OpenMeteoForecastResponseND {
        return withContext(dispatcherProvider.IO) {
            httpClient.get(OPEN_METEO_URL) {
                parameter("latitude", KYIV_LAT.toString())
                parameter("longitude", KYIV_LON.toString())
                parameter("current_weather", true)
                parameter("timezone", "Europe/Kyiv")
                parameter("temperature_unit", "celsius")
                parameter("windspeed_unit", "kmh")
            }.body()
        }
    }
}

private const val OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast"
private const val KYIV_LAT = 50.4501
private const val KYIV_LON = 30.5234
