package io.pylyp.weather.ui.skytrack.calendar.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.common.sharekit.ShareManager
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.usecase.ObserveWeatherObservationLogsUseCase
import io.pylyp.weather.ui.share.toShareJsonArray
import io.pylyp.weather.ui.skytrack.model.ObservationCalendarDayUi
import io.pylyp.weather.ui.skytrack.model.countObservationsByDayOfMonth
import io.pylyp.weather.ui.skytrack.model.shiftMonth
import io.pylyp.weather.ui.skytrack.model.toWeatherObservationRecordUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal class SkyTrackCalendarStoreFactory(
    private val factory: StoreFactory,
    private val observeLogsUseCase: ObserveWeatherObservationLogsUseCase,
    private val shareManager: ShareManager,
    private val initialFocusDay: ObservationCalendarDayUi,
    private val initialVisibleYear: Int,
    private val initialVisibleMonth: Int,
) {

    fun create(): SkyTrackCalendarStore =
        object : SkyTrackCalendarStore,
            Store<SkyTrackCalendarStore.Intent, SkyTrackCalendarStore.State, SkyTrackCalendarStore.Label> by factory.create(
                name = "SkyTrackCalendarStore",
                initialState = SkyTrackCalendarStore.State(
                    focusDay = initialFocusDay,
                    visibleYear = initialVisibleYear,
                    visibleMonth = initialVisibleMonth,
                    countsByDay = emptyMap(),
                    isLoading = true,
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data object SubscribeAction : Action
    }

    private sealed interface Message {
        data class CountsMessage(val countsByDay: Map<Int, Int>) : Message
        data class LoadingMessage(val isLoading: Boolean) : Message
        data class VisibleMonthMessage(val year: Int, val month: Int) : Message
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.SubscribeAction)
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
            SkyTrackCalendarStore.Intent,
            Action,
            SkyTrackCalendarStore.State,
            Message,
            SkyTrackCalendarStore.Label,
            >() {

        private var observeJob: Job? = null
        private var lastObservations: List<WeatherObservationRecordDD> = emptyList()

        override fun executeIntent(intent: SkyTrackCalendarStore.Intent) {
            when (intent) {
                SkyTrackCalendarStore.Intent.BackIntent -> publish(SkyTrackCalendarStore.Label.BackLabel)
                SkyTrackCalendarStore.Intent.PreviousMonthIntent -> {
                    val s = state()
                    val (y, m) = shiftMonth(s.visibleYear, s.visibleMonth, -1)
                    dispatch(Message.VisibleMonthMessage(year = y, month = m))
                    dispatchCountsForVisibleMonth()
                }

                SkyTrackCalendarStore.Intent.NextMonthIntent -> {
                    val s = state()
                    val (y, m) = shiftMonth(s.visibleYear, s.visibleMonth, 1)
                    dispatch(Message.VisibleMonthMessage(year = y, month = m))
                    dispatchCountsForVisibleMonth()
                }

                is SkyTrackCalendarStore.Intent.SelectDayIntent -> {
                    val s = state()
                    publish(
                        SkyTrackCalendarStore.Label.DaySelectedLabel(
                            day = ObservationCalendarDayUi(
                                year = s.visibleYear,
                                monthNumber = s.visibleMonth,
                                dayOfMonth = intent.dayOfMonth,
                            ),
                        ),
                    )
                }
                SkyTrackCalendarStore.Intent.ShareAllIntent -> {
                    if (lastObservations.isNotEmpty()) {
                        val records = lastObservations.map { it.toWeatherObservationRecordUi() }
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
                    lastObservations = emptyList()
                    dispatch(Message.LoadingMessage(isLoading = false))
                    dispatch(Message.CountsMessage(countsByDay = emptyMap()))
                }
                .onEach { list ->
                    lastObservations = list
                    dispatch(Message.LoadingMessage(isLoading = false))
                    dispatchCountsForVisibleMonth()
                }
                .launchIn(scope)
        }

        private fun dispatchCountsForVisibleMonth() {
            val s = state()
            val counts = countObservationsByDayOfMonth(
                records = lastObservations,
                year = s.visibleYear,
                monthNumber = s.visibleMonth,
            )
            dispatch(Message.CountsMessage(countsByDay = counts))
        }
    }

    private object ReducerImpl : Reducer<SkyTrackCalendarStore.State, Message> {
        override fun SkyTrackCalendarStore.State.reduce(msg: Message): SkyTrackCalendarStore.State {
            return when (msg) {
                is Message.LoadingMessage -> copy(isLoading = msg.isLoading)
                is Message.CountsMessage -> copy(countsByDay = msg.countsByDay)
                is Message.VisibleMonthMessage -> copy(visibleYear = msg.year, visibleMonth = msg.month)
            }
        }
    }
}
