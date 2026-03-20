package io.pylyp.weather.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.pylyp.core.threading.DispatcherProvider
import io.pylyp.weather.data.network.entity.MetNoForecastCompactND
import kotlinx.coroutines.withContext

public interface MetNorwayRemoteDataSource {
    public suspend fun getKyivCompactForecast(): MetNoForecastCompactND
}

internal class MetNorwayRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val dispatcherProvider: DispatcherProvider,
) : MetNorwayRemoteDataSource {

    override suspend fun getKyivCompactForecast(): MetNoForecastCompactND {
        return withContext(dispatcherProvider.IO) {
            httpClient.get(MET_NO_URL) {
                parameter("lat", KYIV_LAT.toString())
                parameter("lon", KYIV_LON.toString())
                header(HttpHeaders.UserAgent, MET_NO_USER_AGENT)
            }.body()
        }
    }
}

private const val MET_NO_URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact"
private const val KYIV_LAT = 50.4501
private const val KYIV_LON = 30.5234


private const val MET_NO_USER_AGENT = "KmpDecomposeMviTemplate/1.0 (weather demo; +https://github.com)"
