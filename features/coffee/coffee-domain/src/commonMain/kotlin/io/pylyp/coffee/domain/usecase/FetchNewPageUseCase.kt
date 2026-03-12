package io.pylyp.coffee.domain.usecase

import io.pylyp.coffee.domain.repository.CoffeeRepository
import io.pylyp.common.core.domain.SuspendUseCase

public class FetchNewPageUseCase(
    private val repository: CoffeeRepository,
) : SuspendUseCase<Unit>() {
    override suspend fun execute(parameters: Unit) {
        repository.fetchNewPackImages()
    }
}