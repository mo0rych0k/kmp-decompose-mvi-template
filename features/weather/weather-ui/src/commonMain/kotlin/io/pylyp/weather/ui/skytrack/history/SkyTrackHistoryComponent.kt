package io.pylyp.weather.ui.skytrack.history

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.weather.ui.di.createSkyTrackHistoryStore
import io.pylyp.weather.ui.skytrack.history.store.SkyTrackHistoryStore
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.CoroutineScope

internal interface SkyTrackHistoryComponent {
    val state: Value<SkyTrackHistoryStore.State>
    fun onIntent(intent: SkyTrackHistoryStore.Intent)
}

internal class DefaultSkyTrackHistoryComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    private val output: (Output) -> Unit,
) : SkyTrackHistoryComponent, ComponentContext by componentContext, KoinComponent {

    private val store: SkyTrackHistoryStore = instanceKeeper.getStore { componentFactory.createSkyTrackHistoryStore() }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<SkyTrackHistoryStore.State> = store.asValue()

    init {
        store.subscribe(scope = componentScope) { label ->
            when (label) {
                SkyTrackHistoryStore.Label.BackLabel -> output(Output.Finished)
                SkyTrackHistoryStore.Label.OpenAddLabel -> output(Output.OpenAdd)
                is SkyTrackHistoryStore.Label.OpenDetailsLabel -> output(Output.OpenDetails(recordId = label.recordId))
            }
        }
    }

    override fun onIntent(intent: SkyTrackHistoryStore.Intent) {
        store.accept(intent)
    }

    sealed interface Output {
        data object Finished : Output
        data object OpenAdd : Output
        data class OpenDetails(val recordId: Long) : Output
    }
}
