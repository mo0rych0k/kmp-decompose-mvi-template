package io.pylyp.weather.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.pylyp.core.threading.DispatcherProvider
import io.pylyp.weather.data.network.entity.OpenWeatherCurrentResponseND
import kotlinx.coroutines.withContext

public interface OpenWeatherRemoteDataSource {
    public suspend fun getCurrentWeather(latitude: Double, longitude: Double): OpenWeatherCurrentResponseND
}

internal class OpenWeatherRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val dispatcherProvider: DispatcherProvider,
) : OpenWeatherRemoteDataSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): OpenWeatherCurrentResponseND {
        val key = openWeatherApiKey()
        if (key.isBlank()) {
            error("OpenWeather API key is not configured. Set OPENWEATHER_API_KEY environment variable.")
        }
        return withContext(dispatcherProvider.IO) {
            httpClient.get(OPEN_WEATHER_URL) {
                parameter("lat", latitude.toString())
                parameter("lon", longitude.toString())
                parameter("units", "metric")
                parameter("appid", key)
            }.body()
        }
    }
}

private const val OPEN_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather"
