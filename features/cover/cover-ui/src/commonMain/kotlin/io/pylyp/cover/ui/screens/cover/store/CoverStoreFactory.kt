package io.pylyp.cover.ui.screens.cover.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

internal class CoverStoreFactory(
    private val factory: StoreFactory,
) {

    fun create(): CoverStore =
        object : CoverStore,
            Store<CoverStore.Intent, CoverStore.State, CoverStore.Label> by factory.create(
                name = "CoverStore",
                initialState = CoverStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data class LoadingAction(val isLoading: Boolean) : Action
    }

    private sealed interface Message {
        data class LoadingMessage(val isLoading: Boolean) : Message
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<CoverStore.Intent, Action, CoverStore.State, Message, CoverStore.Label>() {
        override fun executeIntent(intent: CoverStore.Intent) {
            when (intent) {
                is CoverStore.Intent.BackPressedIntent ->
                    publish(label = CoverStore.Label.BackPressedLabel)

                CoverStore.Intent.OnNavigateToCoffeeIntent ->
                    publish(label = CoverStore.Label.OnNavigateToCoffeeLabel)
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.LoadingAction ->
                    dispatch(message = Message.LoadingMessage(isLoading = action.isLoading))
            }
        }
    }

    private object ReducerImpl : Reducer<CoverStore.State, Message> {
        override fun CoverStore.State.reduce(msg: Message): CoverStore.State {
            return when (msg) {
                is Message.LoadingMessage -> copy(isLoading = msg.isLoading)
            }
        }
    }

    private class BootstrapperImpl :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }
}
