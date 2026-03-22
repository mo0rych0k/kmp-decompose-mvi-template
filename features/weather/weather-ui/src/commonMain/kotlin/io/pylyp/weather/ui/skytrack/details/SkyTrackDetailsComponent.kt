package io.pylyp.weather.ui.skytrack.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.weather.ui.di.createSkyTrackDetailsStore
import io.pylyp.weather.ui.skytrack.details.store.SkyTrackDetailsStore
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.CoroutineScope

internal interface SkyTrackDetailsComponent {
    val state: Value<SkyTrackDetailsStore.State>
    fun onIntent(intent: SkyTrackDetailsStore.Intent)
}

internal class DefaultSkyTrackDetailsComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    recordId: Long,
    private val output: (Output) -> Unit,
) : SkyTrackDetailsComponent, ComponentContext by componentContext, KoinComponent {

    private val store: SkyTrackDetailsStore =
        instanceKeeper.getStore { componentFactory.createSkyTrackDetailsStore(recordId = recordId) }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<SkyTrackDetailsStore.State> = store.asValue()

    init {
        store.subscribe(scope = componentScope) { label ->
            when (label) {
                SkyTrackDetailsStore.Label.BackLabel -> output(Output.Back)
                SkyTrackDetailsStore.Label.DeletedLabel -> output(Output.Deleted)
            }
        }
    }

    override fun onIntent(intent: SkyTrackDetailsStore.Intent) {
        store.accept(intent)
    }

    sealed interface Output {
        data object Back : Output
        data object Deleted : Output
    }
}
