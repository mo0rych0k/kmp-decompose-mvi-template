package io.pylyp.weather.ui.skytrack.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.common.sharekit.UrlOpener
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.weather.ui.di.createSkyTrackDetailsStore
import io.pylyp.weather.ui.skytrack.details.store.SkyTrackDetailsStore
import kotlinx.coroutines.CoroutineScope

internal interface SkyTrackDetailsComponent {
    val state: Value<SkyTrackDetailsStore.State>
    fun onIntent(intent: SkyTrackDetailsStore.Intent)
    fun openServiceUrl(url: String)
}

internal class DefaultSkyTrackDetailsComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    private val urlOpener: UrlOpener,
    recordId: Long,
    private val output: (Output) -> Unit,
) : SkyTrackDetailsComponent, ComponentContext by componentContext {

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

    override fun openServiceUrl(url: String) {
        urlOpener.openUrl(url)
    }

    sealed interface Output {
        data object Back : Output
        data object Deleted : Output
    }
}
