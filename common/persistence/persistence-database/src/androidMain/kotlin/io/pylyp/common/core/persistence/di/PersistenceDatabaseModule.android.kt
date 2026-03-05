package io.pylyp.common.core.persistence.di

import android.content.Context
import androidx.room.Room
import io.pylyp.common.core.persistence.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module


public actual val persistenceDatabasePlatformModule: Module = module {
    single<AppDatabase> {
        val appContext: Context = get()
        val dbFile = appContext.getDatabasePath(DB_NAME)
        Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).build()
    }
}