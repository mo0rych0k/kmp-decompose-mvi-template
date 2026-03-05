package io.pylyp.coffee.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.pylyp.coffee.data.network.entity.CoffeeImageND
import io.pylyp.core.threading.DispatcherProvider
import kotlinx.coroutines.withContext

public interface CoffeeRemoteDataSource {
    public suspend fun getPhotoCoffee(): CoffeeImageND
}

internal class CoffeeRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val dispatcherProvider: DispatcherProvider,
) : CoffeeRemoteDataSource {
    override suspend fun getPhotoCoffee(): CoffeeImageND {
        return withContext(dispatcherProvider.IO) {
            httpClient.get("$BASE_URL$ENDPOINT_GET_IMAGE").body()
        }
    }
}

private const val ENDPOINT_GET_IMAGE = "/random.json"

private const val BASE_URL = "https://coffee.alexflipnote.dev"