package io.pylyp.weather.ui.roating

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.weather.domain.entity.WeatherServiceType
import io.pylyp.weather.ui.di.createWeatherDetailsComponent
import io.pylyp.weather.ui.di.createWeatherServicesComponent
import io.pylyp.weather.ui.screens.details.DefaultWeatherDetailsComponent
import io.pylyp.weather.ui.screens.details.WeatherDetailsComponent
import io.pylyp.weather.ui.screens.services.DefaultWeatherServicesComponent
import io.pylyp.weather.ui.screens.services.WeatherServicesComponent
import kotlinx.serialization.Serializable

public interface WeatherRootComponent : BackHandlerOwner {
    public val stack: Value<ChildStack<*, Child>>

    public sealed interface Child
}

internal data class ServicesChild(val component: WeatherServicesComponent) : WeatherRootComponent.Child

internal data class DetailsChild(val component: WeatherDetailsComponent) : WeatherRootComponent.Child

internal class DefaultWeatherRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    private val onFinished: () -> Unit,
) : ComponentContext by componentContext, WeatherRootComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, WeatherRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Select,
            handleBackButton = false,
            childFactory = ::child,
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): WeatherRootComponent.Child =
        when (config) {
            Config.Select -> ServicesChild(
                component = componentFactory.createWeatherServicesComponent(
                    componentContext = componentContext,
                    output = { output ->
                        when (output) {
                            is DefaultWeatherServicesComponent.Output.OpenDetails ->
                                onOpenDetails(service = output.service)

                            DefaultWeatherServicesComponent.Output.Finished ->
                                onFinished()
                        }
                    },
                ),
            )

            is Config.Details -> DetailsChild(
                component = componentFactory.createWeatherDetailsComponent(
                    componentContext = componentContext,
                    serviceType = config.service,
                    output = { output ->
                        when (output) {
                            DefaultWeatherDetailsComponent.Output.Finished -> navigation.pop()
                        }
                    },
                ),
            )
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun onOpenDetails(service: WeatherServiceType) {
        navigation.push(Config.Details(service = service))
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Select : Config

        @Serializable
        data class Details(val service: WeatherServiceType) : Config
    }
}
