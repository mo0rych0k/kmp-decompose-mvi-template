package io.pylyp.coffee.domain.di

import io.pylyp.coffee.domain.usecase.CoffeeListFlowUseCase
import io.pylyp.coffee.domain.usecase.FetchNewPageUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

public val coffeeDomainModule: Module = module {
    singleOf(::CoffeeListFlowUseCase)
    singleOf(::FetchNewPageUseCase)

}