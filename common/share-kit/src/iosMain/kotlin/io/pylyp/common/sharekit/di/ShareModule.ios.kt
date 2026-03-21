package io.pylyp.common.sharekit.di

import io.pylyp.common.sharekit.IosShareManager
import io.pylyp.common.sharekit.IosUrlOpener
import io.pylyp.common.sharekit.ShareManager
import io.pylyp.common.sharekit.UrlOpener
import org.koin.core.module.Module
import org.koin.dsl.module

public actual val shareModule: Module = module {
    single<ShareManager> { IosShareManager() }
    single<UrlOpener> { IosUrlOpener() }
}
