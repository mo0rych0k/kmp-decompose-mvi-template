package io.pylyp.sample.composeapp.di

import io.pylyp.common.app.info.AppEnvironment
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual val platformModule: Module = module {
    single {
        AppEnvironment(
            isDebug = Platform.isDebugBinary
        )
    }
}
