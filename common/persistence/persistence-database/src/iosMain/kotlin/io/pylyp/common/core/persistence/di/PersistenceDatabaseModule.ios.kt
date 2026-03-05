package io.pylyp.common.core.persistence.di

import androidx.room.Room.databaseBuilder
import io.pylyp.common.core.persistence.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask


public actual val persistenceDatabasePlatformModule: Module = module {
    single<AppDatabase> {
        val dbFilePath = documentDirectory() + "/$DB_NAME"
        databaseBuilder<AppDatabase>(
            name = dbFilePath,
        ).build()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}