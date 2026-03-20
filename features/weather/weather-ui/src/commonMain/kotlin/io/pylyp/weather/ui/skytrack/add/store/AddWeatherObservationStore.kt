package io.pylyp.weather.ui.skytrack.add.store

import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.domain.entity.WindDirectionDD

internal interface AddWeatherObservationStore :
    Store<AddWeatherObservationStore.Intent, AddWeatherObservationStore.State, AddWeatherObservationStore.Label> {
    sealed interface Intent {
        data object BackIntent : Intent
        data object SaveIntent : Intent
        data class TemperatureChangedIntent(val value: Double) : Intent
        data class WindStrengthChangedIntent(val value: Int) : Intent
        data class WindDirectionChangedIntent(val value: WindDirectionDD) : Intent
        data class WeatherTypeChangedIntent(val value: WeatherTypeDD) : Intent
    }

    data class State(
        val isLoadingBackground: Boolean = true,
        val apiData: CommonWeatherDD? = null,
        val coordinates: GeoCoordinatesDD? = null,
        /** Reverse-geocoded locality (mobile). */
        val locationLabel: String? = null,
        val loadError: String? = null,
        val userTemperatureC: Double = 20.0,
        val userWindStrengthPercent: Int = 30,
        val userWindDirection: WindDirectionDD = WindDirectionDD.NORTH,
        val userWeatherType: WeatherTypeDD = WeatherTypeDD.SUNNY,
        val isSaving: Boolean = false,
    )

    sealed interface Label {
        data object BackLabel : Label
        data object SavedLabel : Label
    }
}
