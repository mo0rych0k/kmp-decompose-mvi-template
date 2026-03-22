package io.pylyp.weather.ui.skytrack.add.store

import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.weather.domain.entity.CommonWeatherDD
import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.domain.entity.WindDirectionDD

internal const val SAVE_ERROR_MISSING_BACKGROUND_KEY: String = "SAVE_ERROR_MISSING_BACKGROUND"

public enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT,
}

internal interface AddWeatherObservationStore :
    Store<AddWeatherObservationStore.Intent, AddWeatherObservationStore.State, AddWeatherObservationStore.Label> {
    sealed interface Intent {
        data object BackIntent : Intent
        data object SaveIntent : Intent
        data object OpenWindSetupIntent : Intent
        data object CloseWindSetupIntent : Intent
        data class TemperatureChangedIntent(val value: Double) : Intent
        data object TemperatureUnitToggleIntent : Intent
        data class WindStrengthChangedIntent(val value: Int) : Intent
        data class WindDirectionDegreesIntent(val degrees: Float) : Intent
        data class WeatherTypeToggledIntent(val value: WeatherTypeDD) : Intent
    }

    data class State(
        val isLoadingBackground: Boolean = true,
        val apiData: CommonWeatherDD? = null,
        val coordinates: GeoCoordinatesDD? = null,
        /** Reverse-geocoded locality (mobile). */
        val locationLabel: String? = null,
        val loadError: String? = null,
        val userTemperatureC: Double = 0.0,
        val temperatureUnit: TemperatureUnit = TemperatureUnit.CELSIUS,
        val userWindStrengthPercent: Int = 0,
        val userWindDirection: WindDirectionDD = WindDirectionDD.NORTH,
        /** 0° = from north, clockwise; kept in sync with [userWindDirection] when using the wind screen. */
        val windDirectionDegrees: Float = 0f,
        val isWindSetupVisible: Boolean = false,
        val userWeatherTypes: Set<WeatherTypeDD> = emptySet(),
        val isSaving: Boolean = false,
        /** Set when persistence fails after Save; cleared on next save attempt. */
        val saveError: String? = null,
    )

    sealed interface Label {
        data object BackLabel : Label
        data object SavedLabel : Label
    }
}
