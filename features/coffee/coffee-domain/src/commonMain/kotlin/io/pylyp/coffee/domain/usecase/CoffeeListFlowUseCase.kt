package io.pylyp.coffee.domain.usecase

import io.pylyp.coffee.domain.entity.CoffeeImageDD
import io.pylyp.coffee.domain.repository.CoffeeRepository
import io.pylyp.common.core.foundation.entity.FlowUseCase
import kotlinx.coroutines.flow.Flow

public class CoffeeListFlowUseCase(
    private val repository: CoffeeRepository,
) : FlowUseCase<Unit, List<CoffeeImageDD>>() {
    override fun execute(parameters: Unit): Flow<List<CoffeeImageDD>> {
        return repository.getAllImagesFlow()
    }
}
