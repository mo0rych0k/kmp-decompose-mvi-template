package io.pylyp.sample.composeapp.roating

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import io.pylyp.coffee.ui.di.createCoffeeRootComponent
import io.pylyp.coffee.ui.roating.CoffeeRootComponent
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.AppFeature
import io.pylyp.cover.ui.di.createCoverRootComponent
import io.pylyp.cover.ui.roating.CoverRootComponent
import io.pylyp.sample.composeapp.roating.AppRootComponent.Child.Coffee
import io.pylyp.sample.composeapp.roating.AppRootComponent.Child.Cover
import io.pylyp.sample.composeapp.roating.mapper.toConfig
import kotlinx.serialization.Serializable

public interface AppRootComponent {
    public val stack: Value<ChildStack<*, Child>>

    public sealed class Child {
        public class Cover(public val component: CoverRootComponent) : Child()
        public class Coffee(public val component: CoffeeRootComponent) : Child()
    }
}

public class DefaultAppRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, AppRootComponent {

    private val navigation = StackNavigation<AppRootConfig>()

    override val stack: Value<ChildStack<*, AppRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = AppRootConfig.serializer(),
            initialConfiguration = AppRootConfig.Cover,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: AppRootConfig,
        componentContext: ComponentContext,
    ): AppRootComponent.Child =
        when (config) {
            AppRootConfig.Cover -> Cover(
                component = componentFactory.createCoverRootComponent(
                    componentContext = componentContext,
                    onCloseFeature = ::onCloseFeature,
                    onNavigateToFeature = ::onNavigateToFeature
                ),
            )

            AppRootConfig.Coffee -> Coffee(
                component = componentFactory.createCoffeeRootComponent(
                    componentContext = componentContext,
                ),
            )
        }

    private fun onCloseFeature() {
        navigation.pop()
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun onNavigateToFeature(appFeature: AppFeature) {
        navigation.push(appFeature.toConfig())
    }

    @Serializable
    internal sealed interface AppRootConfig {
        @Serializable
        data object Cover : AppRootConfig

        @Serializable
        data object Coffee : AppRootConfig
    }
}