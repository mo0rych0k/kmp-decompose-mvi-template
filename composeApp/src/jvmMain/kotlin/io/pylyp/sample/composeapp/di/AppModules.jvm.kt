package io.pylyp.sample.composeapp.di


import io.pylyp.common.app.info.AppEnvironment
import org.koin.core.module.Module
import org.koin.dsl.module


internal actual val platformModule: Module = module {
    single {
        AppEnvironment(
//            false only for JVM
            isDebug = false
        )
    }
}
