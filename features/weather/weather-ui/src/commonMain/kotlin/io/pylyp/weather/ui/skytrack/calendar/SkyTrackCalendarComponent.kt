package io.pylyp.weather.ui.skytrack.calendar

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.weather.ui.di.createSkyTrackCalendarStore
import io.pylyp.weather.ui.skytrack.calendar.store.SkyTrackCalendarStore
import io.pylyp.weather.ui.skytrack.model.ObservationCalendarDayUi
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.CoroutineScope

internal interface SkyTrackCalendarComponent {
    val state: Value<SkyTrackCalendarStore.State>
    fun onIntent(intent: SkyTrackCalendarStore.Intent)
}

internal class DefaultSkyTrackCalendarComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    visibleYear: Int,
    visibleMonth: Int,
    focusDay: ObservationCalendarDayUi,
    private val output: (Output) -> Unit,
) : SkyTrackCalendarComponent, ComponentContext by componentContext, KoinComponent {

    private val store: SkyTrackCalendarStore = instanceKeeper.getStore {
        componentFactory.createSkyTrackCalendarStore(
            initialFocusDay = focusDay,
            initialVisibleYear = visibleYear,
            initialVisibleMonth = visibleMonth,
        )
    }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<SkyTrackCalendarStore.State> = store.asValue()

    private val backCallback = BackCallback {
        store.accept(SkyTrackCalendarStore.Intent.BackIntent)
    }

    init {
        backHandler.register(backCallback)
        store.subscribe(scope = componentScope) { label ->
            when (label) {
                SkyTrackCalendarStore.Label.BackLabel -> output(Output.Finished)
                is SkyTrackCalendarStore.Label.GoToTodayLabel ->
                    output(Output.GoToToday(today = label.today))

                is SkyTrackCalendarStore.Label.DaySelectedLabel ->
                    output(Output.DaySelected(day = label.day))
            }
        }
    }

    override fun onIntent(intent: SkyTrackCalendarStore.Intent) {
        store.accept(intent)
    }

    sealed interface Output {
        data object Finished : Output
        data class GoToToday(val today: ObservationCalendarDayUi) : Output
        data class DaySelected(val day: ObservationCalendarDayUi) : Output
    }
}
