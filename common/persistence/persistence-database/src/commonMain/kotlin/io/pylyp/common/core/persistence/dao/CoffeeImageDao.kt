package io.pylyp.common.core.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.pylyp.common.core.persistence.entity.CoffeeImageSD
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CoffeeImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CoffeeImageSD>)

    @Query("SELECT * FROM CoffeeImageSD")
    fun getFlow(): Flow<List<CoffeeImageSD>>
}