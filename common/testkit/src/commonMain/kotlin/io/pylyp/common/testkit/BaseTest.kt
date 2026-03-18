package io.pylyp.common.testkit

import io.pylyp.common.core.di.IsolatedKoinContext
import io.pylyp.core.threading.test.di.testDispatchersModule
import io.pylyp.core.threading.test.testDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Extend this from your test class and call [beforeTest] / [afterTest]
 * from `@BeforeTest` / `@AfterTest` in the concrete test.
 */
public abstract class BaseTest {

    /**
     * Return app/test modules for this test.
     * `DispatcherProvider` is pre-registered by [BaseTest] before these modules.
     */
    protected open fun koinModules(): List<Module> = listOf(
        module {
            single {
                Json {
                    ignoreUnknownKeys = true
                    /**
                     * For pretty indents
                     */
                    prettyPrint = true
                    explicitNulls = false
                }
            }
        },
        testDispatchersModule,
    )

    public open fun beforeTest() {
        Dispatchers.setMain(testDispatcher)

        IsolatedKoinContext.startKoin(
            modules = koinModules() + getDiModules(),
        )
    }

    public abstract fun getDiModules(): List<Module>

    public open fun afterTest() {
        IsolatedKoinContext.stop()
        Dispatchers.resetMain()
    }
}

