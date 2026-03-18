package io.pylyp.api.core.di

import io.pylyp.api.core.client.provideHttpClientEngine
import org.koin.core.module.Module
import org.koin.dsl.module

public val networkEngineModule: Module = module {
    single { provideHttpClientEngine().create() }
}