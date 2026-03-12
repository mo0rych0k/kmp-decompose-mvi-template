package io.pylyp.coffee.ui.screens.gallery

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.coffee.ui.di.createGalleryStore
import io.pylyp.coffee.ui.screens.gallery.DefaultGalleryComponent.Output.OpenDetails
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.core.component.KoinComponent

internal interface GalleryComponent {
    val state: Value<GalleryStore.State>
    fun onIntent(intent: GalleryStore.Intent)
    fun moveToIndex(index: Int)
}

@OptIn(InternalCoroutinesApi::class)
internal class DefaultGalleryComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    output: (Output) -> Unit,
) : ComponentContext by componentContext, GalleryComponent, KoinComponent {

    private val store: GalleryStore =
        instanceKeeper.getStore { componentFactory.createGalleryStore() }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<GalleryStore.State> = store.asValue()

    sealed interface Output {
        data class OpenDetails(val index: Int) : Output
        data object OnBack : Output
    }

    sealed class Input {
        data class ShowedPositionImage(val index: Int) : Input()
    }

    override fun moveToIndex(index: Int) {
        store.accept(intent = GalleryStore.Intent.SetVisibleItemIntent(index = index))
    }

    init {
        store.subscribe(
            scope = componentScope,
        ) { label ->
            val outputData = when (label) {
                is GalleryStore.Label.OpenDetailsLabel -> OpenDetails(index = label.index)
                GalleryStore.Label.OnBackLabel -> Output.OnBack
            }
            output(outputData)
        }
    }

    override fun onIntent(intent: GalleryStore.Intent) {
        store.accept(intent)
    }
}