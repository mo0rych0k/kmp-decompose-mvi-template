package io.pylyp.weather.ui.skytrack

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
import io.pylyp.weather.ui.di.createAddWeatherObservationComponent
import io.pylyp.weather.ui.di.createSkyTrackDetailsComponent
import io.pylyp.weather.ui.di.createSkyTrackHistoryComponent
import io.pylyp.weather.ui.skytrack.add.AddWeatherObservationComponent
import io.pylyp.weather.ui.skytrack.add.DefaultAddWeatherObservationComponent
import io.pylyp.weather.ui.skytrack.details.DefaultSkyTrackDetailsComponent
import io.pylyp.weather.ui.skytrack.details.SkyTrackDetailsComponent
import io.pylyp.weather.ui.skytrack.history.DefaultSkyTrackHistoryComponent
import io.pylyp.weather.ui.skytrack.history.SkyTrackHistoryComponent
import kotlinx.serialization.Serializable

public interface SkyTrackRootComponent : BackHandlerOwner {
    public val stack: Value<ChildStack<*, Child>>

    public sealed interface Child
}

internal data class SkyTrackHistoryChild(val component: SkyTrackHistoryComponent) : SkyTrackRootComponent.Child

internal data class SkyTrackAddChild(val component: AddWeatherObservationComponent) : SkyTrackRootComponent.Child

internal data class SkyTrackDetailsChild(val component: SkyTrackDetailsComponent) : SkyTrackRootComponent.Child

@OptIn(DelicateDecomposeApi::class)
internal class DefaultSkyTrackRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    private val onFinished: () -> Unit,
) : ComponentContext by componentContext, SkyTrackRootComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, SkyTrackRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.History,
            handleBackButton = false,
            childFactory = ::child,
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): SkyTrackRootComponent.Child =
        when (config) {
            Config.History -> SkyTrackHistoryChild(
                component = componentFactory.createSkyTrackHistoryComponent(
                    componentContext = componentContext,
                    output = { output ->
                        when (output) {
                            DefaultSkyTrackHistoryComponent.Output.Finished -> onFinished()
                            DefaultSkyTrackHistoryComponent.Output.OpenAdd -> navigation.push(Config.Add)
                            is DefaultSkyTrackHistoryComponent.Output.OpenDetails ->
                                navigation.push(Config.Details(recordId = output.recordId))
                        }
                    },
                ),
            )

            Config.Add -> SkyTrackAddChild(
                component = componentFactory.createAddWeatherObservationComponent(
                    componentContext = componentContext,
                    output = { output ->
                        when (output) {
                            DefaultAddWeatherObservationComponent.Output.Back -> navigation.pop()
                            DefaultAddWeatherObservationComponent.Output.Saved -> navigation.pop()
                        }
                    },
                ),
            )

            is Config.Details -> SkyTrackDetailsChild(
                component = componentFactory.createSkyTrackDetailsComponent(
                    componentContext = componentContext,
                    recordId = config.recordId,
                    output = { output ->
                        when (output) {
                            DefaultSkyTrackDetailsComponent.Output.Back -> navigation.pop()
                            DefaultSkyTrackDetailsComponent.Output.Deleted -> navigation.pop()
                        }
                    },
                ),
            )
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object History : Config

        @Serializable
        data object Add : Config

        @Serializable
        data class Details(val recordId: Long) : Config
    }
}
