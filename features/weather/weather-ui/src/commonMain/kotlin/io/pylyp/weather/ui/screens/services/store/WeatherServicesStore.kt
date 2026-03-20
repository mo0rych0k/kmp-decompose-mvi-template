package io.pylyp.weather.ui.screens.services.store

import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.domain.entity.WeatherServiceType

internal interface WeatherServicesStore :
    Store<WeatherServicesStore.Intent, WeatherServicesStore.State, WeatherServicesStore.Label> {

    sealed interface Intent {
        data class OnServiceClick(val service: WeatherServiceType) : Intent
        data object BackPressedIntent : Intent
    }

    data class State(
        val services: List<WeatherServiceType> = WeatherServiceType.entries,
    )

    sealed interface Label {
        data class OpenDetailsLabel(val service: WeatherServiceType) : Label
        data object FinishedLabel : Label
    }
}
