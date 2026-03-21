package io.pylyp.weather.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.pylyp.core.threading.DispatcherProvider
import io.pylyp.weather.data.network.entity.OpenMeteoCurrentForecastResponseND
import kotlinx.coroutines.withContext

public interface OpenMeteoRemoteDataSource {
    public suspend fun getCurrentWeather(latitude: Double, longitude: Double): OpenMeteoCurrentForecastResponseND
}

internal class OpenMeteoRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val dispatcherProvider: DispatcherProvider,
) : OpenMeteoRemoteDataSource {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): OpenMeteoCurrentForecastResponseND {
        return withContext(dispatcherProvider.IO) {
            httpClient.get(OPEN_METEO_FORECAST_URL) {
                parameter("latitude", latitude.toString())
                parameter("longitude", longitude.toString())
                parameter(
                    "current",
                    "temperature_2m,relative_humidity_2m,surface_pressure," +
                        "wind_speed_10m,wind_direction_10m,weather_code",
                )
                parameter("timezone", "auto")
                parameter("temperature_unit", "celsius")
                parameter("windspeed_unit", "kmh")
            }.body()
        }
    }
}

private const val OPEN_METEO_FORECAST_URL = "https://api.open-meteo.com/v1/forecast"
