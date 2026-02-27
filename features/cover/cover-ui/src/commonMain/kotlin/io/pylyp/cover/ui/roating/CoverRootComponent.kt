package io.pylyp.cover.ui.roating

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.pylyp.core.di.ComponentFactory
import io.pylyp.cover.ui.di.createCoverComponent
import io.pylyp.cover.ui.screens.cover.CoverComponent
import kotlinx.serialization.Serializable

public interface CoverRootComponent {
    public val stack: Value<ChildStack<*, Child>>

    public sealed class Child {
        public data class Cover(val component: CoverComponent) : Child()
    }
}

internal class DefaultCoverRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, CoverRootComponent {

    private val navigation = StackNavigation<Config>()


    override val stack: Value<ChildStack<*, CoverRootComponent.Child>> =
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
    ): CoverRootComponent.Child =
        when (config) {
            Config.Cover -> CoverRootComponent.Child.Cover(
                componentFactory.createCoverComponent(
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