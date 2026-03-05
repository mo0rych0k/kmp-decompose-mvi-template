package io.pylyp.coffee.data.network.di

import io.pylyp.coffee.data.network.CoffeeRemoteDataSource
import io.pylyp.coffee.data.network.CoffeeRemoteDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val coffeeDataNetworkModule: Module = module {
    factoryOf(::CoffeeRemoteDataSourceImpl) bind CoffeeRemoteDataSource::class
}