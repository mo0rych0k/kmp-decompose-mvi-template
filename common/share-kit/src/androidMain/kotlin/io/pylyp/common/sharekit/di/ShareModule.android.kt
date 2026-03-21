package io.pylyp.common.sharekit.di

import android.content.Context
import io.pylyp.common.sharekit.AndroidShareManager
import io.pylyp.common.sharekit.AndroidUrlOpener
import io.pylyp.common.sharekit.ShareManager
import io.pylyp.common.sharekit.UrlOpener
import org.koin.core.module.Module
import org.koin.dsl.module

public actual val shareModule: Module = module {
    single<ShareManager> { AndroidShareManager(get<Context>()) }
    single<UrlOpener> { AndroidUrlOpener(get<Context>()) }
}
