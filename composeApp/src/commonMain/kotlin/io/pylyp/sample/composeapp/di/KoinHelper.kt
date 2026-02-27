package io.pylyp.sample.composeapp.di

import io.pylyp.core.di.IsolatedKoinContext
import org.koin.dsl.KoinAppDeclaration


public fun initKoin(additionalInitialization: KoinAppDeclaration = {}) {
    IsolatedKoinContext.startKoin(
        modules = appModules,
        additionalInitialization = additionalInitialization
    )
}

public fun iOsInitKoin() {
    initKoin { }
}