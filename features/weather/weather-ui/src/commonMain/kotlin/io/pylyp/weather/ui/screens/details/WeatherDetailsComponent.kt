package io.pylyp.weather.ui.screens.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.weather.domain.entity.WeatherServiceType
import io.pylyp.weather.ui.di.createWeatherDetailsStore
import io.pylyp.weather.ui.screens.details.store.WeatherDetailsStore
import org.koin.core.component.KoinComponent

internal interface WeatherDetailsComponent {
    val state: Value<WeatherDetailsStore.State>
    fun onIntent(intent: WeatherDetailsStore.Intent)
}

internal class DefaultWeatherDetailsComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    serviceType: WeatherServiceType,
    private val output: (Output) -> Unit,
) : ComponentContext by componentContext, WeatherDetailsComponent, KoinComponent {

    private val store: WeatherDetailsStore =
        instanceKeeper.getStore {
            componentFactory.createWeatherDetailsStore(serviceType = serviceType)
        }

    override val state: Value<WeatherDetailsStore.State> = store.asValue()

    sealed interface Output {
        data object Finished : Output
    }

    private val backCallback = BackCallback {
        output(Output.Finished)
    }

    init {
        backHandler.register(backCallback)
    }

    override fun onIntent(intent: WeatherDetailsStore.Intent) {
        store.accept(intent)
    }
}
