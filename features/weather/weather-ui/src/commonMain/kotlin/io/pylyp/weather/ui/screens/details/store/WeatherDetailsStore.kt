package io.pylyp.weather.ui.screens.details.store

import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.domain.entity.WeatherServiceType

internal interface WeatherDetailsStore :
    Store<WeatherDetailsStore.Intent, WeatherDetailsStore.State, WeatherDetailsStore.Label> {

    sealed interface Intent {
        data object RetryIntent : Intent
    }

    data class State(
        val serviceType: WeatherServiceType,
        val serviceDisplayName: String,
        val locationName: String = "Kyiv",
        val isLoading: Boolean = true,
        val temperatureText: String? = null,
        val conditionText: String? = null,
        val windText: String? = null,
        val humidityText: String? = null,
        val observedAtText: String? = null,
        val errorMessage: String? = null,
    )

    sealed interface Label
}
