package io.pylyp.common.core.persistence.di

import io.pylyp.common.core.persistence.CoffeeImagesStorage
import io.pylyp.common.core.persistence.CoffeeImagesStorageImpl
import io.pylyp.common.core.persistence.db.DatabaseCreator
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val persistenceDatabaseModule: Module = module {
    singleOf(::DatabaseCreator)
    singleOf(::CoffeeImagesStorageImpl) bind CoffeeImagesStorage::class
}

public expect val persistenceDatabasePlatformModule: Module

internal const val DB_NAME = "appDatabase"