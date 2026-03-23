package io.pylyp.common.sharekit.di

import io.pylyp.common.sharekit.JvmShareManager
import io.pylyp.common.sharekit.ShareManager
import org.koin.core.module.Module
import org.koin.dsl.module

public actual val shareModule: Module = module {
    single<ShareManager> { JvmShareManager() }
}
