package io.pylyp.weather.ui.screens.services

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.weather.ui.di.createWeatherServicesStore
import io.pylyp.weather.ui.screens.services.store.WeatherServicesStore
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.CoroutineScope

internal interface WeatherServicesComponent {
    val state: Value<WeatherServicesStore.State>
    fun onIntent(intent: WeatherServicesStore.Intent)
}

internal class DefaultWeatherServicesComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    private val output: (Output) -> Unit,
) : ComponentContext by componentContext, WeatherServicesComponent, KoinComponent {

    private val store: WeatherServicesStore =
        instanceKeeper.getStore { componentFactory.createWeatherServicesStore() }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<WeatherServicesStore.State> = store.asValue()

    sealed interface Output {
        data class OpenDetails(val service: io.pylyp.weather.domain.entity.WeatherServiceType) : Output
        data object Finished : Output
    }

    private val backCallback = BackCallback {
        store.accept(intent = WeatherServicesStore.Intent.BackPressedIntent)
    }

    init {
        backHandler.register(backCallback)
        store.subscribe(scope = componentScope) { label ->
            when (label) {
                is WeatherServicesStore.Label.OpenDetailsLabel ->
                    output(Output.OpenDetails(service = label.service))

                WeatherServicesStore.Label.FinishedLabel ->
                    output(Output.Finished)
            }
        }
    }

    override fun onIntent(intent: WeatherServicesStore.Intent) {
        store.accept(intent)
    }
}
