package io.pylyp.common.sharekit.di

import org.koin.core.module.Module

/**
 * Platform-specific Koin module that provides [io.pylyp.common.sharekit.ShareManager].
 * Use [org.koin.android.ext.koin.androidContext] when initializing Koin to provide `Context` for
 * [io.pylyp.common.sharekit.ShareManager] on Android.
 */
public expect val shareModule: Module
