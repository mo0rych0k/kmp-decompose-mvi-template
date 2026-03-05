package io.pylyp.common.core.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import io.pylyp.common.core.persistence.dao.CoffeeImageDao
import io.pylyp.common.core.persistence.entity.CoffeeImageSD


@Database(
    entities = [
        CoffeeImageSD::class,
    ],
    version = 1,
)

internal abstract class AppDatabase : RoomDatabase() {
    internal abstract fun coffeeImageDao(): CoffeeImageDao
}
