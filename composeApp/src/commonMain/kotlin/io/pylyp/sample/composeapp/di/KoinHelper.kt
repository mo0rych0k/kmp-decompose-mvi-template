package io.pylyp.sample.composeapp.di

import io.pylyp.common.core.di.IsolatedKoinContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
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
public fun initKoinQQQ(appDeclaration: KoinAppDeclaration? = null): KoinApplication = startKoin {
    appDeclaration?.invoke(this)
    modules(appModules)
}

