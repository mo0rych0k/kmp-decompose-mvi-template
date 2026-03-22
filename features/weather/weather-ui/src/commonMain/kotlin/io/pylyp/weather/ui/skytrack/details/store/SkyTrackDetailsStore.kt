package io.pylyp.weather.ui.skytrack.details.store

import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD

internal interface SkyTrackDetailsStore :
    Store<SkyTrackDetailsStore.Intent, SkyTrackDetailsStore.State, SkyTrackDetailsStore.Label> {
    sealed interface Intent {
        data object BackIntent : Intent
        data object DeleteIntent : Intent
        data object RetryIntent : Intent
    }

    data class State(
        val recordId: Long,
        val record: WeatherObservationRecordDD? = null,
        val isLoading: Boolean = true,
        val errorMessage: String? = null,
    )

    sealed interface Label {
        data object BackLabel : Label
        data object DeletedLabel : Label
    }
}
