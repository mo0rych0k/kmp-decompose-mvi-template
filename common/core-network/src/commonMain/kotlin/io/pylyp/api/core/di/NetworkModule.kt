package io.pylyp.api.core.di

import io.pylyp.api.core.client.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

public val networkModule: Module = module {
    single { createHttpClient(get()) }
}