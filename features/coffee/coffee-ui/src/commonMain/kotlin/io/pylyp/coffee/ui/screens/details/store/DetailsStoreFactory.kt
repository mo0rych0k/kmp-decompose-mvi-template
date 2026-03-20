package io.pylyp.coffee.ui.screens.details.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.coffee.domain.usecase.CoffeeListFlowUseCase
import io.pylyp.coffee.ui.screens.gallery.entity.CoffeeImageUiData
import io.pylyp.coffee.ui.screens.gallery.mapper.toUi
import io.pylyp.common.core.foundation.entity.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class DetailsStoreFactory(
    private val factory: StoreFactory,
    private val coffeeListFlowUseCase: CoffeeListFlowUseCase,
    private val showedImageIndex: Int,
) {

    fun create(): DetailsStore =
        object : DetailsStore,
            Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> by factory.create(
                name = "DetailsStore",
                initialState = DetailsStore.State(
                    showedImageIndex = showedImageIndex
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data class CoffeeListLoaded(val images: List<CoffeeImageUiData>) : Action
    }

    private sealed interface Message {
        data class CoffeeListLoaded(val images: List<CoffeeImageUiData>) : Message
        data class OnPageChanged(val index: Int) : Message
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<DetailsStore.Intent, Action, DetailsStore.State,
                Message, DetailsStore.Label>() {
        override fun executeIntent(intent: DetailsStore.Intent) {
            when (intent) {
                is DetailsStore.Intent.OnPageChanged -> {
                    dispatch(
                        message = Message.OnPageChanged(
                            index = intent.index
                        )
                    )
                }
            }

        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.CoffeeListLoaded ->
                    dispatch(message = Message.CoffeeListLoaded(images = action.images))
            }
        }
    }

    private object ReducerImpl : Reducer<DetailsStore.State, Message> {
        override fun DetailsStore.State.reduce(msg: Message): DetailsStore.State {
            return when (msg) {
                is Message.CoffeeListLoaded -> copy(images = msg.images)
                is Message.OnPageChanged -> copy(currentIndexPage = msg.index)
            }
        }
    }

    private inner class BootstrapperImpl :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            coffeeListFlowUseCase(parameters = Unit)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Error -> Unit
                        is Resource.Idle -> Unit
                        is Resource.Loading -> Unit
                        is Resource.Success -> {
                            dispatch(Action.CoffeeListLoaded(images = resource.data.map { it.toUi() }))
                        }
                    }
                }
                .launchIn(scope)
        }
    }
}
