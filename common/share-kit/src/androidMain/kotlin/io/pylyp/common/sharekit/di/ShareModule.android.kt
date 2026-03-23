package io.pylyp.common.sharekit.di

import android.content.Context
import io.pylyp.common.sharekit.AndroidShareManager
import io.pylyp.common.sharekit.ShareManager
import org.koin.core.module.Module
import org.koin.dsl.module

public actual val shareModule: Module = module {
    single<ShareManager> { AndroidShareManager(get<Context>()) }
}
