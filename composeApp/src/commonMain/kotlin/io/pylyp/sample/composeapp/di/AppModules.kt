package io.pylyp.sample.composeapp.di

import org.koin.core.module.Module

internal val appModules: List<Module> = listOf(
    appNavigationModule,
)