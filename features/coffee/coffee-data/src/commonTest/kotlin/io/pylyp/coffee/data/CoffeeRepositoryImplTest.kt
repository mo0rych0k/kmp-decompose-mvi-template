package io.pylyp.coffee.data

import io.pylyp.coffee.data.network.CoffeeRemoteDataSource
import io.pylyp.coffee.data.network.entity.CoffeeImageND
import io.pylyp.common.core.persistence.CoffeeImagesStorage
import io.pylyp.common.core.persistence.entity.CoffeeImageSD
import io.pylyp.common.testkit.BaseTest
import io.pylyp.core.threading.test.testDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.koin.core.module.Module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CoffeeRepositoryImplTest : BaseTest() {

    @BeforeTest
    fun setup() = beforeTest()

    @AfterTest
    fun tearDown() = afterTest()

    @Test
    fun `getAllImagesFlow maps storage entities to domain`() = runTest(testDispatcher) {
        val storage = FakeCoffeeImagesStorage(
            initial = listOf(
                CoffeeImageSD(id = 1, url = "u1"),
                CoffeeImageSD(id = 2, url = "u2"),
            ),
        )
        val repo = CoffeeRepositoryImpl(
            coffeeRemoteDataSource = FakeCoffeeRemoteDataSource(),
            coffeeImagesStorage = storage,
        )

        val items = repo.getAllImagesFlow().first()

        assertEquals(listOf(1L, 2L), items.map { it.id })
        assertEquals(listOf("u1", "u2"), items.map { it.url })
    }

    @Test
    fun `fetchNewPackImages inserts only successful remote results`() = runTest(testDispatcher) {
        val storage = FakeCoffeeImagesStorage(initial = emptyList())
        val remote = FakeCoffeeRemoteDataSource(
            next = listOf(
                Result.success(CoffeeImageND(file = "a")),
                Result.failure(IllegalStateException("boom")),
                Result.success(CoffeeImageND(file = "b")),
            ),
        )
        val repo = CoffeeRepositoryImpl(
            coffeeRemoteDataSource = remote,
            coffeeImagesStorage = storage,
        )

        repo.fetchNewPackImages()

        assertTrue(storage.inserted.isNotEmpty(), "expected repository to insert at least one item")
        assertEquals(setOf("a", "b"), storage.inserted.map { it.url }.toSet())
    }

    override fun getDiModules(): List<Module> = emptyList()

    private class FakeCoffeeImagesStorage(
        initial: List<CoffeeImageSD>,
    ) : CoffeeImagesStorage {
        private val flow = MutableStateFlow(initial)

        val inserted: MutableList<CoffeeImageSD> = mutableListOf()

        override suspend fun insert(items: List<CoffeeImageSD>) {
            inserted += items
            flow.value = flow.value + items
        }

        override fun getFlow(): Flow<List<CoffeeImageSD>> = flow
    }

    private class FakeCoffeeRemoteDataSource(
        next: List<Result<CoffeeImageND>> = emptyList(),
    ) : CoffeeRemoteDataSource {
        private val results = ArrayDeque(next)
        var calls: Int = 0
            private set

        override suspend fun getPhotoCoffee(): CoffeeImageND {
            calls++
            val result = results.removeFirstOrNull()
            if (result != null) return result.getOrThrow()
            throw NoSuchElementException("No more prepared results")
        }
    }
}
