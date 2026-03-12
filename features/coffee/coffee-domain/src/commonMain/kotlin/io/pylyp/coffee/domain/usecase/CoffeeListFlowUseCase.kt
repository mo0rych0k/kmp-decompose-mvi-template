package io.pylyp.coffee.domain.usecase

import io.pylyp.coffee.domain.entity.CoffeeImageDD
import io.pylyp.coffee.domain.repository.CoffeeRepository
import kotlinx.coroutines.flow.Flow

public class CoffeeListFlowUseCase(
    private val repository: CoffeeRepository,
) {
    public operator fun invoke(): Flow<List<CoffeeImageDD>> {
        return repository.getAllImagesFlow()
    }
}