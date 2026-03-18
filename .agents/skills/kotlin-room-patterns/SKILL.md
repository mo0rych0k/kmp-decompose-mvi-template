---
name: kotlin-room-patterns
description: AndroidX Room persistence patterns for Kotlin Multiplatform — entity definitions, DAOs, Database, and DI integration.
---

# Kotlin Room Patterns

Follow these practices when working with AndroidX Room in KMP:

## 1. Entity Definitions

- Use `@Entity` with a clear table name.
- Primary keys should be named `id` by default.
- Use `@PrimaryKey(autoGenerate = true)` for local-only IDs, but prefer domain IDs if they come from
  the server.
- Keep entities in the `data` layer, map to domain models in repositories.

```kotlin
@Entity(tableName = "coffee_images")
data class CoffeeImageEntity(
    @PrimaryKey val id: String,
    val url: String,
    val author: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

## 2. DAOs (Data Access Objects)

- DAOs should be interfaces in `commonMain`.
- Use `@Dao` annotation.
- Always use `suspend` for write operations (Insert, Update, Delete).
- Use `Flow<List<T>>` for reactive read operations.

```kotlin
@Dao
interface CoffeeImageDao {
    @Query("SELECT * FROM coffee_images ORDER BY timestamp DESC")
    fun getAllImages(): Flow<List<CoffeeImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<CoffeeImageEntity>)

    @Query("DELETE FROM coffee_images")
    suspend fun clear()
}
```

## 3. Database Class

- Inherit from `RoomDatabase`.
- Must be an `expect` class in `commonMain` or use the new Room Multiplatform approach.
- In this template, it is usually managed in `:common:persistence:persistence-database`.

```kotlin
@Database(entities = [CoffeeImageEntity::class], version = 1)
@ConstructiveRoomDatabase
abstract class AppDatabase : RoomDatabase() {
    abstract fun coffeeImageDao(): CoffeeImageDao
}
```

## 4. DI Integration (Koin)

- Provide the database and DAOs in a Koin module.
- Use `scope.get<AppDatabase>().coffeeImageDao()` to provide DAOs.

```kotlin
val databaseModule = module {
    single {
        get<RoomDatabase.Builder<AppDatabase>>().build()
    }
    single { get<AppDatabase>().coffeeImageDao() }
}
```

## 5. Migrations and Schemas

- Always provide a `schemaDirectory` in `build.gradle.kts`.
- Use automated migrations if possible; otherwise, define explicit `Migration` objects.

## 6. Testing

- Use an in-memory database for unit tests.
- Room Multiplatform tests usually require platform-specific builders.
