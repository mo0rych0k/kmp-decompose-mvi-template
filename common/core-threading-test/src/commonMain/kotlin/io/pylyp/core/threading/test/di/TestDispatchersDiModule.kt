package io.pylyp.core.threading.test.di

import io.pylyp.core.threading.DispatcherProvider
import io.pylyp.core.threading.test.TestDispatcherProviderImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val testDispatchersModule: Module = module {
    singleOf(::TestDispatcherProviderImpl) bind DispatcherProvider::class
}