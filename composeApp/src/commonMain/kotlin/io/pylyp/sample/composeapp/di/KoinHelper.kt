package io.pylyp.sample.composeapp.di

import io.pylyp.network.core.di.IsolatedKoinContext
import org.koin.dsl.KoinAppDeclaration


public fun initKoin(additionalInitialization: KoinAppDeclaration = {}) {
    IsolatedKoinContext.startKoin(
        modules = appModules,
        additionalInitialization = additionalInitialization
    )
}

public fun initKoinInPlatform() {
    initKoin { }
}
