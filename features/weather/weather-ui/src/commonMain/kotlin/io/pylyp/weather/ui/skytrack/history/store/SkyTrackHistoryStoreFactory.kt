package io.pylyp.weather.ui.skytrack.history.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.weather.domain.usecase.ObserveWeatherObservationLogsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal class SkyTrackHistoryStoreFactory(
    private val factory: StoreFactory,
    private val observeLogsUseCase: ObserveWeatherObservationLogsUseCase,
) {

    fun create(): SkyTrackHistoryStore =
        object : SkyTrackHistoryStore,
            Store<SkyTrackHistoryStore.Intent, SkyTrackHistoryStore.State, SkyTrackHistoryStore.Label> by factory.create(
                name = "SkyTrackHistoryStore",
                initialState = SkyTrackHistoryStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data object SubscribeAction : Action
    }

    private sealed interface Message {
        data class RecordsMessage(val records: List<io.pylyp.weather.domain.entity.WeatherObservationRecordDD>) :
            Message

        data class LoadingMessage(val isLoading: Boolean) : Message
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.SubscribeAction)
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
            SkyTrackHistoryStore.Intent,
            Action,
            SkyTrackHistoryStore.State,
            Message,
            SkyTrackHistoryStore.Label,
            >() {

        private var observeJob: Job? = null

        override fun executeIntent(intent: SkyTrackHistoryStore.Intent) {
            when (intent) {
                SkyTrackHistoryStore.Intent.BackIntent -> publish(SkyTrackHistoryStore.Label.BackLabel)
                SkyTrackHistoryStore.Intent.OpenAddIntent -> publish(SkyTrackHistoryStore.Label.OpenAddLabel)
                is SkyTrackHistoryStore.Intent.OpenDetailsIntent ->
                    publish(SkyTrackHistoryStore.Label.OpenDetailsLabel(recordId = intent.recordId))
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.SubscribeAction -> subscribe()
            }
        }

        private fun subscribe() {
            observeJob?.cancel()
            observeJob = observeLogsUseCase.observe()
                .onStart {
                    dispatch(Message.LoadingMessage(isLoading = true))
                }
                .catch {
                    dispatch(Message.LoadingMessage(isLoading = false))
                    dispatch(Message.RecordsMessage(records = emptyList()))
                }
                .onEach { list ->
                    dispatch(Message.LoadingMessage(isLoading = false))
                    dispatch(Message.RecordsMessage(records = list))
                }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<SkyTrackHistoryStore.State, Message> {
        override fun SkyTrackHistoryStore.State.reduce(msg: Message): SkyTrackHistoryStore.State {
            return when (msg) {
                is Message.LoadingMessage -> copy(isLoading = msg.isLoading)
                is Message.RecordsMessage -> copy(records = msg.records)
            }
        }
    }
}
