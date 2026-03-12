package com.pylyp.sample.di

import com.pylyp.sample.BuildConfig
import io.pylyp.common.app.info.AppEnvironment
import org.koin.core.module.Module
import org.koin.dsl.module

internal val androidAppModule: Module = module {
    single {
        AppEnvironment(
            isDebug = BuildConfig.DEBUG
        )
    }
}