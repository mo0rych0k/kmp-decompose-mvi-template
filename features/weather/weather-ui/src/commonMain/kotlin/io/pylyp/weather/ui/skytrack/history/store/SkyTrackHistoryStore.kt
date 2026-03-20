package io.pylyp.weather.ui.skytrack.history.store

import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD

internal interface SkyTrackHistoryStore :
    Store<SkyTrackHistoryStore.Intent, SkyTrackHistoryStore.State, SkyTrackHistoryStore.Label> {
    sealed interface Intent {
        data object BackIntent : Intent
        data object OpenAddIntent : Intent
        data class OpenDetailsIntent(val recordId: Long) : Intent
    }

    data class State(
        val records: List<WeatherObservationRecordDD> = emptyList(),
        val isLoading: Boolean = true,
    )

    sealed interface Label {
        data object BackLabel : Label
        data object OpenAddLabel : Label
        data class OpenDetailsLabel(val recordId: Long) : Label
    }
}
