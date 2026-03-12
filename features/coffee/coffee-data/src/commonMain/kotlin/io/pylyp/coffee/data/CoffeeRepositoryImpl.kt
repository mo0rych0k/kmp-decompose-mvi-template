package io.pylyp.coffee.data

import io.pylyp.coffee.data.mappers.toDomain
import io.pylyp.coffee.data.mappers.toStorage
import io.pylyp.coffee.data.network.CoffeeRemoteDataSource
import io.pylyp.coffee.domain.entity.CoffeeImageDD
import io.pylyp.coffee.domain.repository.CoffeeRepository
import io.pylyp.common.core.persistence.CoffeeImagesStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

internal class CoffeeRepositoryImpl(
    private val coffeeRemoteDataSource: CoffeeRemoteDataSource,
    private val coffeeImagesStorage: CoffeeImagesStorage,
) : CoffeeRepository {
    override fun getAllImagesFlow(): Flow<List<CoffeeImageDD>> {
        return coffeeImagesStorage.getFlow()
            .map { list -> list.map { item -> item.toDomain() } }
    }

    override suspend fun fetchNewPackImages() {
        val page = supervisorScope {
            (1..COUNT_ITEMS_IN_PAGE).map {
                async {
                    try {
                        coffeeRemoteDataSource.getPhotoCoffee()
                    } catch (_: Exception) {
                        null
                    }
                }
            }.awaitAll()
        }

        if (page.isEmpty()) return

        coffeeImagesStorage.insert(page.mapNotNull { it?.toStorage() })
    }
}

private const val COUNT_ITEMS_IN_PAGE = 10