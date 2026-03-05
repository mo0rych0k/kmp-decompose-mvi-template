package io.pylyp.common.core.persistence.db

import io.pylyp.common.core.persistence.AppDatabase
import io.pylyp.core.threading.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

internal class DatabaseCreator(
    private val database: AppDatabase,
    private val dispatcherProvider: DispatcherProvider,
) : KoinComponent {

    internal val dbFlow: Flow<AppDatabase> by lazy {
        flow { emit(database) }
            .flowOn(dispatcherProvider.IO)
    }

    internal suspend fun getDb(): AppDatabase = withContext(dispatcherProvider.IO) { database }
}


