package io.pylyp.weather.ui.screens.details.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.common.core.foundation.entity.Resource
import io.pylyp.weather.domain.entity.CurrentWeatherDD
import io.pylyp.weather.domain.entity.WeatherServiceType
import io.pylyp.weather.domain.entity.displayName
import io.pylyp.weather.domain.usecase.GetKyivWeatherUseCase
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class WeatherDetailsStoreFactory(
    private val factory: StoreFactory,
    private val kyivWeatherUseCase: GetKyivWeatherUseCase,
    private val serviceType: WeatherServiceType,
) {

    fun create(): WeatherDetailsStore =
        object : WeatherDetailsStore,
            Store<WeatherDetailsStore.Intent, WeatherDetailsStore.State, WeatherDetailsStore.Label> by factory.create(
                name = "WeatherDetailsStore",
                initialState = WeatherDetailsStore.State(
                    serviceType = serviceType,
                    serviceDisplayName = serviceType.displayName(),
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data object LoadAction : Action
    }

    private sealed interface Message {
        data class LoadingMessage(val isLoading: Boolean) : Message
        data class SuccessMessage(val data: CurrentWeatherDD) : Message
        data class ErrorMessage(val message: String) : Message
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
            WeatherDetailsStore.Intent,
            Action,
            WeatherDetailsStore.State,
            Message,
            WeatherDetailsStore.Label,
            >() {

        private var loadJob: Job? = null

        override fun executeIntent(intent: WeatherDetailsStore.Intent) {
            when (intent) {
                WeatherDetailsStore.Intent.RetryIntent -> forward(Action.LoadAction)
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.LoadAction -> loadWeather()
            }
        }

        private fun loadWeather() {
            loadJob?.cancel()
            val weatherFlow: Flow<Resource<CurrentWeatherDD>> =
                kyivWeatherUseCase(serviceType)
            loadJob = weatherFlow
                .onEach { resource: Resource<CurrentWeatherDD> ->
                    when (resource) {
                        Resource.Idle -> Unit
                        Resource.Loading ->
                            dispatch(message = Message.LoadingMessage(isLoading = true))

                        is Resource.Success ->
                            dispatch(message = Message.SuccessMessage(data = resource.data))

                        is Resource.Error ->
                            dispatch(
                                message = Message.ErrorMessage(
                                    message = resource.message,
                                ),
                            )
                    }
                }
                .launchIn(scope)
        }
    }

    private object ReducerImpl : Reducer<WeatherDetailsStore.State, Message> {
        override fun WeatherDetailsStore.State.reduce(msg: Message): WeatherDetailsStore.State {
            return when (msg) {
                is Message.LoadingMessage -> copy(
                    isLoading = msg.isLoading,
                    errorMessage = null,
                )

                is Message.SuccessMessage -> copy(
                    isLoading = false,
                    errorMessage = null,
                    locationName = msg.data.locationName,
                    temperatureText = msg.data.temperatureC?.let(::formatTemperatureC),
                    conditionText = msg.data.conditionDescription,
                    windText = msg.data.windSpeedKmh?.let(::formatWindKmh),
                    humidityText = msg.data.humidityPercent?.let { humidity -> "$humidity%" },
                    observedAtText = msg.data.observedAt,
                )

                is Message.ErrorMessage -> copy(
                    isLoading = false,
                    errorMessage = msg.message,
                )
            }
        }
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(action = Action.LoadAction)
        }
    }
}

private fun formatTemperatureC(celsius: Double): String {
    val scaled = (celsius * 10).roundToInt()
    val whole = scaled / 10
    val frac = abs(scaled % 10)
    return "$whole.$frac °C"
}

private fun formatWindKmh(kmh: Double): String = "${kmh.roundToInt()} km/h"
