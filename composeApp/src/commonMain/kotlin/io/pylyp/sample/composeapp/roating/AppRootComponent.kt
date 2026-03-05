package io.pylyp.sample.composeapp.roating

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.pylyp.cover.ui.di.createCoverRootComponent
import io.pylyp.cover.ui.roating.CoverRootComponent
import io.pylyp.network.core.di.ComponentFactory
import kotlinx.serialization.Serializable

internal interface AppRootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Cover(val component: CoverRootComponent) : Child()
    }
}

internal class DefaultAppRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, AppRootComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, AppRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Cover,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): AppRootComponent.Child =
        when (config) {
            Config.Cover -> AppRootComponent.Child.Cover(
                component = componentFactory.createCoverRootComponent(
                    componentContext = componentContext,
                ),
            )
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Cover : Config
    }
}