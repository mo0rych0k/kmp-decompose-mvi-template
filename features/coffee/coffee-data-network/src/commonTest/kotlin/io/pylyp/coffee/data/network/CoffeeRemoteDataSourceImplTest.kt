package io.pylyp.coffee.data.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.pylyp.api.core.di.networkModule
import io.pylyp.coffee.data.network.di.coffeeDataNetworkModule
import io.pylyp.common.core.di.IsolatedKoinContext
import io.pylyp.common.testkit.BaseTest
import io.pylyp.core.threading.test.testDispatcher
import kotlinx.coroutines.test.runTest
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CoffeeRemoteDataSourceImplTest : BaseTest() {

    @BeforeTest
    fun setup() {
        beforeTest()
    }

    @Test
    fun `getPhotoCoffee decodes json`() = runTest(testDispatcher) {
        val coffeeRemoteDataSource: CoffeeRemoteDataSource = IsolatedKoinContext.koin().get()
        val result = coffeeRemoteDataSource.getPhotoCoffee()

        assertEquals("https://example.com/coffee.png", result.file)
    }

    override fun getDiModules(): List<Module> {
        val mockEngine = MockEngine {
            respond(
                content = """{"file":"https://example.com/coffee.png"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                ),
            )
        }
        return listOf(
            coffeeDataNetworkModule,
            networkModule,
        ) + module {
            single { mockEngine } bind HttpClientEngine::class
        }
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }
}
