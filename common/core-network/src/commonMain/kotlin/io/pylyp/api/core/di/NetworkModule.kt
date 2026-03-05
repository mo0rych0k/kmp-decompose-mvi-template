package io.pylyp.api.core.di

import io.pylyp.api.core.client.createHttpClient
import io.pylyp.api.core.client.provideHttpClientEngine
import org.koin.core.module.Module
import org.koin.dsl.module

public val networkModule: Module = module {
    single { provideHttpClientEngine() }
    single { createHttpClient(get()) }
}