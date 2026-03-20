package io.pylyp.coffee.domain.usecase

import io.pylyp.coffee.domain.repository.CoffeeRepository
import io.pylyp.common.core.foundation.entity.SuspendUseCase

public class FetchNewPageUseCase(
    private val repository: CoffeeRepository,
) : SuspendUseCase<Unit, Unit>() {
    override suspend fun execute(parameters: Unit) {
        repository.fetchNewPackImages()
    }
}
