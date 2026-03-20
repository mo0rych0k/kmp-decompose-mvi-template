package io.pylyp.common.core.persistence

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import io.pylyp.common.core.persistence.dao.CoffeeImageDao
import io.pylyp.common.core.persistence.entity.CoffeeImageSD


@Database(
    entities = [
        CoffeeImageSD::class,
    ],
    version = 1,
)
@ConstructedBy(AppDatabaseConstructor::class)
internal abstract class AppDatabase : RoomDatabase() {
    internal abstract fun coffeeImageDao(): CoffeeImageDao
}

@Suppress("KotlinNoActualForExpect")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
