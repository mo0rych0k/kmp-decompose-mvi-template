package io.pylyp.cover.ui.screens.cover

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.cover.ui.di.createCoverStore
import io.pylyp.cover.ui.screens.cover.store.CoverStore
import io.pylyp.network.core.di.ComponentFactory
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

internal interface CoverComponent {
    val state: Value<CoverStore.State>
    fun onIntent(intent: CoverStore.Intent)
}

internal class DefaultCoverComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
) : ComponentContext by componentContext, CoverComponent, KoinComponent {

    private val store: CoverStore = instanceKeeper.getStore { componentFactory.createCoverStore() }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<CoverStore.State> = store.asValue()

    init {
        store.subscribe(
            scope = componentScope,
        ) { label ->
            when (label) {
                CoverStore.Label.BackPressedLabel -> TODO()
            }
        }
    }

    override fun onIntent(intent: CoverStore.Intent) {
        store.accept(intent)
    }
}