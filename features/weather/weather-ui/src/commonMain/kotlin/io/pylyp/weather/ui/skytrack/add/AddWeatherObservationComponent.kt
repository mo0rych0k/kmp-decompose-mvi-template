package io.pylyp.weather.ui.skytrack.add

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.weather.ui.di.createAddWeatherObservationStore
import io.pylyp.weather.ui.skytrack.add.store.AddWeatherObservationStore
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.CoroutineScope

internal interface AddWeatherObservationComponent {
    val state: Value<AddWeatherObservationStore.State>
    fun onIntent(intent: AddWeatherObservationStore.Intent)
}

internal class DefaultAddWeatherObservationComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    private val output: (Output) -> Unit,
) : AddWeatherObservationComponent, ComponentContext by componentContext, KoinComponent {

    private val store: AddWeatherObservationStore =
        instanceKeeper.getStore { componentFactory.createAddWeatherObservationStore() }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<AddWeatherObservationStore.State> = store.asValue()

    init {
        store.subscribe(scope = componentScope) { label ->
            when (label) {
                AddWeatherObservationStore.Label.BackLabel -> output(Output.Back)
                AddWeatherObservationStore.Label.SavedLabel -> output(Output.Saved)
            }
        }
    }

    override fun onIntent(intent: AddWeatherObservationStore.Intent) {
        store.accept(intent)
    }

    sealed interface Output {
        data object Back : Output
        data object Saved : Output
    }
}
