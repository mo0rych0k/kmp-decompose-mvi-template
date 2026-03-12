package io.pylyp.coffee.ui.roating

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.mvikotlin.core.utils.internal.InternalMviKotlinApi
import io.pylyp.coffee.ui.di.createDetailsComponent
import io.pylyp.coffee.ui.di.createGalleryComponent
import io.pylyp.coffee.ui.screens.details.DefaultDetailsComponent
import io.pylyp.coffee.ui.screens.details.DetailsComponent
import io.pylyp.coffee.ui.screens.gallery.DefaultGalleryComponent
import io.pylyp.coffee.ui.screens.gallery.GalleryComponent
import io.pylyp.common.core.di.ComponentFactory
import kotlinx.serialization.Serializable

public interface CoffeeRootComponent : BackHandlerOwner {
    public val stack: Value<ChildStack<*, Child>>

    public sealed interface Child
}

internal data class Coffee(val component: GalleryComponent) : CoffeeRootComponent.Child
internal data class Details(val component: DetailsComponent) : CoffeeRootComponent.Child

internal class DefaultCoffeeRootComponent(
    componentContext: ComponentContext,
    private val componentFactory: ComponentFactory,
) : ComponentContext by componentContext, CoffeeRootComponent {


    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, CoffeeRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Coffee,
            handleBackButton = false,
            childFactory = ::child,
        )

    @OptIn(InternalMviKotlinApi::class)
    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): CoffeeRootComponent.Child {
        return when (config) {
            is Config.Coffee -> Coffee(
                componentFactory.createGalleryComponent(
                    componentContext = componentContext,
                    output = {
                        when (it) {
                            is DefaultGalleryComponent.Output.OpenDetails ->
                                onOpenDetails(it.index)

                            DefaultGalleryComponent.Output.OnBack ->
                                onBack()
                        }
                    }
                ),
            )

            is Config.Details -> Details(
                componentFactory.createDetailsComponent(
                    componentContext = componentContext,
                    showedImageIndex = config.index,
                    output = {
                        when (it) {
                            is DefaultDetailsComponent.Output.OnFinished -> {
                                navigation.pop()
                                val component = (stack.value.active.instance as Coffee).component
                                component.moveToIndex(it.index)
                            }
                        }
                    },
                ),
            )
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun onOpenDetails(index: Int) {
        navigation.push(Config.Details(index = index))
    }

    fun onBack() {
        stack.subscribe { }
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Coffee : Config

        @Serializable
        data class Details(val index: Int) : Config
    }
}