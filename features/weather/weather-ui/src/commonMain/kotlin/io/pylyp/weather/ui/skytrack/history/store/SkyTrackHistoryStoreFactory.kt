package io.pylyp.weather.ui.skytrack.history.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.common.sharekit.ShareManager
import io.pylyp.weather.domain.usecase.DeleteWeatherObservationUseCase
import io.pylyp.weather.domain.usecase.ObserveWeatherObservationLogsUseCase
import io.pylyp.weather.ui.share.toShareJson
import io.pylyp.weather.ui.share.toShareJsonArray
import io.pylyp.weather.ui.skytrack.model.ObservationCalendarDayUi
import io.pylyp.weather.ui.skytrack.model.isInLocalDay
import io.pylyp.weather.ui.skytrack.model.toWeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.todayObservationCalendarDayUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class SkyTrackHistoryStoreFactory(
    private val factory: StoreFactory,
    private val observeLogsUseCase: ObserveWeatherObservationLogsUseCase,
    private val deleteUseCase: DeleteWeatherObservationUseCase,
    private val shareManager: ShareManager,
    private val initialSelectedDay: ObservationCalendarDayUi?,
) {

    fun create(): SkyTrackHistoryStore {
        val selectedDay = initialSelectedDay ?: todayObservationCalendarDayUi()
        return object : SkyTrackHistoryStore,
            Store<SkyTrackHistoryStore.Intent, SkyTrackHistoryStore.State, SkyTrackHistoryStore.Label> by factory.create(
                name = "SkyTrackHistoryStore",
                initialState = SkyTrackHistoryStore.State(selectedDay = selectedDay),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Action {
        data object SubscribeAction : Action
    }

    private sealed interface Message {
        data class RecordsMessage(
            val totalObservationsInDatabase: Int,
            val records: List<io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi>,
        ) : Message

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
                SkyTrackHistoryStore.Intent.OpenCalendarIntent ->
                    publish(SkyTrackHistoryStore.Label.OpenCalendarLabel(focusDay = state().selectedDay))
                is SkyTrackHistoryStore.Intent.OpenDetailsIntent ->
                    publish(SkyTrackHistoryStore.Label.OpenDetailsLabel(recordId = intent.recordId))
                is SkyTrackHistoryStore.Intent.DeleteRecordIntent -> scope.launch {
                    deleteUseCase(intent.recordId).collect { }
                }

                is SkyTrackHistoryStore.Intent.ShareRecordIntent -> {
                    val json = intent.record.toShareJson()
                    shareManager.shareText(json)
                }

                SkyTrackHistoryStore.Intent.ShareDayIntent -> {
                    val records = state().records
                    if (records.isNotEmpty()) {
                        val json = records.toShareJsonArray()
                        shareManager.shareText(json)
                    }
                }
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
                    dispatch(Message.RecordsMessage(totalObservationsInDatabase = 0, records = emptyList()))
                }
                .onEach { list ->
                    val day = state().selectedDay
                    val filtered = list.filter { it.isInLocalDay(day) }.map { it.toWeatherObservationRecordUi() }
                    dispatch(Message.LoadingMessage(isLoading = false))
                    dispatch(
                        Message.RecordsMessage(
                            totalObservationsInDatabase = list.size,
                            records = filtered,
                        ),
                    )
                }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<SkyTrackHistoryStore.State, Message> {
        override fun SkyTrackHistoryStore.State.reduce(msg: Message): SkyTrackHistoryStore.State {
            return when (msg) {
                is Message.LoadingMessage -> copy(isLoading = msg.isLoading)
                is Message.RecordsMessage ->
                    copy(
                        records = msg.records,
                        totalObservationsInDatabase = msg.totalObservationsInDatabase,
                    )
            }
        }
    }
}
