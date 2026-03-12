package io.pylyp.coffee.domain.repository

import io.pylyp.coffee.domain.entity.CoffeeImageDD
import kotlinx.coroutines.flow.Flow

public interface CoffeeRepository {
    public fun getAllImagesFlow(): Flow<List<CoffeeImageDD>>
    public suspend fun fetchNewPackImages()
}