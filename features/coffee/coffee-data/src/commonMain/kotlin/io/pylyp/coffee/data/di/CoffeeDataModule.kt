package io.pylyp.coffee.data.di

import io.pylyp.coffee.data.CoffeeRepositoryImpl
import io.pylyp.coffee.domain.repositories.CoffeeRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val coffeeDataModule: Module = module {
    factoryOf(::CoffeeRepositoryImpl) bind CoffeeRepository::class
}