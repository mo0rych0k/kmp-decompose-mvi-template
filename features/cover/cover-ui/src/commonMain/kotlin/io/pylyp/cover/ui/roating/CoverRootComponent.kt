package io.pylyp.cover.ui.roating

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.AppFeature
import io.pylyp.cover.ui.di.createCoverComponent
import io.pylyp.cover.ui.roating.CoverRootComponent.Child
import io.pylyp.cover.ui.screens.cover.CoverComponent
import kotlinx.serialization.Serializable

public interface CoverRootComponent {
    public val stack: Value<ChildStack<*, Child>>

    public sealed interface Child
}

internal data class Cover(val component: CoverComponent) : Child

internal class DefaultCoverRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
    private val onCloseFeature: () -> Unit,
    private val onNavigateToFeature: (appFeature: AppFeature) -> Unit,
) : ComponentContext by componentContext, CoverRootComponent {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
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
    ): Child =
        when (config) {
            Config.Cover -> Cover(
                componentFactory.createCoverComponent(
                    componentContext = componentContext,
                    onCloseFeature = onCloseFeature,
                    onNavigateToFeature = onNavigateToFeature
                ),
            )
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Cover : Config
    }
}