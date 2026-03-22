package io.pylyp.weather.ui.skytrack.history.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.ui.skytrack.model.ObservationCalendarDayUi
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi

internal interface SkyTrackHistoryStore :
    Store<SkyTrackHistoryStore.Intent, SkyTrackHistoryStore.State, SkyTrackHistoryStore.Label> {
    sealed interface Intent {
        data object BackIntent : Intent
        data object OpenAddIntent : Intent
        data object OpenCalendarIntent : Intent
        data class OpenDetailsIntent(val recordId: Long) : Intent
        data class DeleteRecordIntent(val recordId: Long) : Intent
        data class ShareRecordIntent(val record: WeatherObservationRecordUi) : Intent
        data object ShareDayIntent : Intent
    }

    @Immutable
    data class State(
        val selectedDay: ObservationCalendarDayUi,
        val records: List<WeatherObservationRecordUi> = emptyList(),
        val totalObservationsInDatabase: Int = 0,
        val isLoading: Boolean = true,
    )

    sealed interface Label {
        data object BackLabel : Label
        data object OpenAddLabel : Label
        data class OpenCalendarLabel(val focusDay: ObservationCalendarDayUi) : Label
        data class OpenDetailsLabel(val recordId: Long) : Label
    }
}
