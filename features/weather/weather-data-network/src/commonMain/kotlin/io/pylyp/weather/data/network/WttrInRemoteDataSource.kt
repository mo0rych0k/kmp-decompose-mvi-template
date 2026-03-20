package io.pylyp.weather.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.pylyp.core.threading.DispatcherProvider
import io.pylyp.weather.data.network.entity.WttrInResponseND
import kotlinx.coroutines.withContext

public interface WttrInRemoteDataSource {
    public suspend fun getKyivJson(): WttrInResponseND
}

internal class WttrInRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val dispatcherProvider: DispatcherProvider,
) : WttrInRemoteDataSource {

    override suspend fun getKyivJson(): WttrInResponseND {
        return withContext(dispatcherProvider.IO) {
            httpClient.get(WTTR_KYIV_URL).body()
        }
    }
}

private const val WTTR_KYIV_URL = "https://wttr.in/Kyiv?format=j1"
