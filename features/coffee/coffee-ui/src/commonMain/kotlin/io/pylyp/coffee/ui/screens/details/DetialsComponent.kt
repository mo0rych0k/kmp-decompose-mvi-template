package io.pylyp.coffee.ui.screens.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.coffee.ui.di.createDetailsStore
import io.pylyp.coffee.ui.screens.details.store.DetailsStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import org.koin.core.component.KoinComponent

internal interface DetailsComponent {
    val state: Value<DetailsStore.State>
    fun onIntent(intent: DetailsStore.Intent)
}

internal class DefaultDetailsComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    output: (output: Output) -> Unit,
    showedImageIndex: Int
) : ComponentContext by componentContext, DetailsComponent, KoinComponent {

    private val store: DetailsStore =
        instanceKeeper.getStore {
            componentFactory.createDetailsStore(
                showedImageIndex = showedImageIndex
            )
        }

    override val state: Value<DetailsStore.State> = store.asValue()

    sealed interface Output {
        data class OnFinished(val index: Int) : Output
    }

    private val backCallback = BackCallback {
        state.value.currentIndexPage?.let {
            output(
                Output.OnFinished(index = state.value.currentIndexPage ?: 0)
            )
        }
    }

    init {
        backHandler.register(backCallback)
    }

    override fun onIntent(intent: DetailsStore.Intent) {
        store.accept(intent)
    }
}