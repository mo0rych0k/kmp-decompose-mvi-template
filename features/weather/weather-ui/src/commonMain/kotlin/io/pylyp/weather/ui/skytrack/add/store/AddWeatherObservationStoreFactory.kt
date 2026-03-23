package io.pylyp.weather.ui.skytrack.add.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.common.core.foundation.entity.Resource
import io.pylyp.weather.domain.entity.SkyTrackBackgroundWeather
import io.pylyp.weather.domain.entity.toCenterBearingDegrees
import io.pylyp.weather.domain.entity.toWindDirectionDD
import io.pylyp.weather.domain.location.PlaceLabelProvider
import io.pylyp.weather.domain.usecase.LoadSkyTrackBackgroundWeatherUseCase
import io.pylyp.weather.domain.usecase.SaveWeatherObservationParams
import io.pylyp.weather.domain.usecase.SaveWeatherObservationUseCase
import io.pylyp.weather.ui.skytrack.add.cloudinessTypes
import io.pylyp.weather.ui.skytrack.model.CommonWeatherUi
import io.pylyp.weather.ui.skytrack.model.GeoCoordinatesUi
import io.pylyp.weather.ui.skytrack.model.WeatherTypeUi
import io.pylyp.weather.ui.skytrack.model.toCommonWeatherDD
import io.pylyp.weather.ui.skytrack.model.toCommonWeatherUi
import io.pylyp.weather.ui.skytrack.model.toGeoCoordinatesDD
import io.pylyp.weather.ui.skytrack.model.toGeoCoordinatesUi
import io.pylyp.weather.ui.skytrack.model.toWeatherTypeDD
import io.pylyp.weather.ui.skytrack.model.toWindDirectionDomain
import io.pylyp.weather.ui.skytrack.model.toWindDirectionUi
import kotlinx.coroutines.launch

internal class AddWeatherObservationStoreFactory(
    private val factory: StoreFactory,
    private val loadBackgroundWeatherUseCase: LoadSkyTrackBackgroundWeatherUseCase,
    private val saveWeatherObservationUseCase: SaveWeatherObservationUseCase,
    private val placeLabelProvider: PlaceLabelProvider,
) {

    fun create(): AddWeatherObservationStore =
        object : AddWeatherObservationStore,
            Store<AddWeatherObservationStore.Intent, AddWeatherObservationStore.State, AddWeatherObservationStore.Label> by factory.create(
                name = "AddWeatherObservationStore",
                initialState = AddWeatherObservationStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data object LoadBackgroundAction : Action
    }

    private sealed interface Message {
        data class BackgroundLoadedMessage(
            val coordinates: GeoCoordinatesUi,
            val api: CommonWeatherUi,
        ) : Message

        data class BackgroundFailedMessage(val message: String) : Message
        data class PlaceLabelMessage(val label: String?) : Message
        data class LoadingBackgroundMessage(val isLoading: Boolean) : Message
        data class TemperatureMessage(val value: Double) : Message
        data class WindStrengthMessage(val value: Int) : Message
        data class WindDirectionDegreesMessage(val degrees: Float) : Message
        data class WeatherTypeToggledMessage(val value: WeatherTypeUi) : Message
        data object OpenWindSetupMessage : Message
        data object CloseWindSetupMessage : Message
        data object TemperatureUnitToggledMessage : Message
        data class SavingMessage(val isSaving: Boolean) : Message

        /** `null` means save succeeded (caller publishes [AddWeatherObservationStore.Label.SavedLabel]). */
        data class SaveFinishedMessage(val error: String?) : Message
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.LoadBackgroundAction)
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
            AddWeatherObservationStore.Intent,
            Action,
            AddWeatherObservationStore.State,
            Message,
            AddWeatherObservationStore.Label,
            >() {

        override fun executeIntent(intent: AddWeatherObservationStore.Intent) {
            when (intent) {
                AddWeatherObservationStore.Intent.BackIntent -> {
                    if (state().isWindSetupVisible) {
                        dispatch(Message.CloseWindSetupMessage)
                    } else {
                        publish(AddWeatherObservationStore.Label.BackLabel)
                    }
                }
                AddWeatherObservationStore.Intent.SaveIntent -> save()
                AddWeatherObservationStore.Intent.OpenWindSetupIntent -> dispatch(Message.OpenWindSetupMessage)
                AddWeatherObservationStore.Intent.CloseWindSetupIntent -> dispatch(Message.CloseWindSetupMessage)
                is AddWeatherObservationStore.Intent.TemperatureChangedIntent ->
                    dispatch(Message.TemperatureMessage(value = intent.value))

                AddWeatherObservationStore.Intent.TemperatureUnitToggleIntent ->
                    dispatch(Message.TemperatureUnitToggledMessage)

                is AddWeatherObservationStore.Intent.WindStrengthChangedIntent ->
                    dispatch(Message.WindStrengthMessage(value = intent.value))

                is AddWeatherObservationStore.Intent.WindDirectionDegreesIntent ->
                    dispatch(Message.WindDirectionDegreesMessage(degrees = intent.degrees))

                is AddWeatherObservationStore.Intent.WeatherTypeToggledIntent ->
                    dispatch(Message.WeatherTypeToggledMessage(value = intent.value))
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.LoadBackgroundAction -> loadBackground()
            }
        }

        private fun loadBackground() {
            scope.launch {
                dispatch(Message.LoadingBackgroundMessage(isLoading = true))
                loadBackgroundWeatherUseCase(Unit).collect { resource: Resource<SkyTrackBackgroundWeather> ->
                    when (resource) {
                        Resource.Idle -> Unit
                        Resource.Loading -> dispatch(Message.LoadingBackgroundMessage(isLoading = true))
                        is Resource.Success -> {
                            val coords = resource.data.coordinates
                            dispatch(
                                Message.BackgroundLoadedMessage(
                                    coordinates = coords.toGeoCoordinatesUi(),
                                    api = resource.data.apiWeather.toCommonWeatherUi(),
                                ),
                            )
                            dispatch(Message.LoadingBackgroundMessage(isLoading = false))
                            scope.launch {
                                val label = placeLabelProvider.resolveLabel(
                                    latitude = coords.latitude,
                                    longitude = coords.longitude,
                                )
                                dispatch(Message.PlaceLabelMessage(label = label))
                            }
                        }

                        is Resource.Error -> {
                            dispatch(Message.BackgroundFailedMessage(message = resource.message))
                            dispatch(Message.LoadingBackgroundMessage(isLoading = false))
                        }
                    }
                }
            }
        }

        private fun save() {
            val state = state()
            val api = state.apiData
            val coords = state.coordinates
            if (api == null || coords == null) {
                dispatch(
                    Message.SaveFinishedMessage(
                        error = SAVE_ERROR_MISSING_BACKGROUND_KEY,
                    ),
                )
                return
            }
            scope.launch {
                dispatch(Message.SavingMessage(isSaving = true))
                saveWeatherObservationUseCase(
                    SaveWeatherObservationParams(
                        coordinates = coords.toGeoCoordinatesDD(),
                        locationLabel = state.locationLabel,
                        userTemperatureC = state.userTemperatureC,
                        userWindDirection = state.userWindDirection.toWindDirectionDomain(),
                        userWindStrengthPercent = state.userWindStrengthPercent,
                        userWeatherTypes = state.userWeatherTypes.map { it.toWeatherTypeDD() }.toSet(),
                        apiWeather = api.toCommonWeatherDD(),
                    ),
                ).collect { resource ->
                    when (resource) {
                        Resource.Idle -> Unit
                        Resource.Loading -> Unit
                        is Resource.Success -> {
                            dispatch(Message.SaveFinishedMessage(error = null))
                            publish(AddWeatherObservationStore.Label.SavedLabel)
                        }

                        is Resource.Error -> {
                            dispatch(
                                Message.SaveFinishedMessage(
                                    error = resource.message.ifBlank { "Save failed" },
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<AddWeatherObservationStore.State, Message> {
        override fun AddWeatherObservationStore.State.reduce(msg: Message): AddWeatherObservationStore.State {
            return when (msg) {
                is Message.LoadingBackgroundMessage -> copy(isLoadingBackground = msg.isLoading)
                is Message.BackgroundLoadedMessage ->
                    copy(
                        isLoadingBackground = false,
                        apiData = msg.api,
                        coordinates = msg.coordinates,
                        locationLabel = null,
                        loadError = null,
                    )

                is Message.PlaceLabelMessage -> copy(locationLabel = msg.label)

                is Message.BackgroundFailedMessage ->
                    copy(isLoadingBackground = false, loadError = msg.message)

                is Message.TemperatureMessage -> copy(userTemperatureC = msg.value)
                is Message.WindStrengthMessage -> copy(userWindStrengthPercent = msg.value)
                is Message.WindDirectionDegreesMessage ->
                    copy(
                        windDirectionDegrees = msg.degrees,
                        userWindDirection = msg.degrees.toWindDirectionDD().toWindDirectionUi(),
                    )

                is Message.OpenWindSetupMessage ->
                    copy(
                        isWindSetupVisible = true,
                        windDirectionDegrees = userWindDirection.toWindDirectionDomain().toCenterBearingDegrees(),
                    )

                is Message.CloseWindSetupMessage -> copy(isWindSetupVisible = false)
                is Message.TemperatureUnitToggledMessage -> copy(
                    temperatureUnit = when (temperatureUnit) {
                        TemperatureUnit.CELSIUS -> TemperatureUnit.FAHRENHEIT
                        TemperatureUnit.FAHRENHEIT -> TemperatureUnit.CELSIUS
                    },
                )

                is Message.WeatherTypeToggledMessage -> {
                    val v = msg.value
                    if (v in cloudinessTypes) {
                        val withoutCloudiness = userWeatherTypes - cloudinessTypes.toSet()
                        copy(userWeatherTypes = withoutCloudiness + v)
                    } else {
                        val next = if (v in userWeatherTypes) {
                            userWeatherTypes - v
                        } else {
                            userWeatherTypes + v
                        }
                        copy(userWeatherTypes = next)
                    }
                }
                is Message.SavingMessage ->
                    copy(
                        isSaving = msg.isSaving,
                        saveError = if (msg.isSaving) null else saveError,
                    )

                is Message.SaveFinishedMessage ->
                    copy(isSaving = false, saveError = msg.error)
            }
        }
    }
}
