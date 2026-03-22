package io.pylyp.weather.ui.skytrack.calendar.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.ui.skytrack.model.ObservationCalendarDayUi

internal interface SkyTrackCalendarStore :
    Store<SkyTrackCalendarStore.Intent, SkyTrackCalendarStore.State, SkyTrackCalendarStore.Label> {
    sealed interface Intent {
        data object BackIntent : Intent
        data object PreviousMonthIntent : Intent
        data object NextMonthIntent : Intent
        data class SelectDayIntent(val dayOfMonth: Int) : Intent
        data object ShareAllIntent : Intent
    }

    @Immutable
    data class State(
        val focusDay: ObservationCalendarDayUi,
        val visibleYear: Int,
        val visibleMonth: Int,
        val countsByDay: Map<Int, Int>,
        val isLoading: Boolean,
    )

    sealed interface Label {
        data object BackLabel : Label
        data class DaySelectedLabel(val day: ObservationCalendarDayUi) : Label
    }
}
