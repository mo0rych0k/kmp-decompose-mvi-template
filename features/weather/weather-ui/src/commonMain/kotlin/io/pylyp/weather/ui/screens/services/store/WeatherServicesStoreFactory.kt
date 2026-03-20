package io.pylyp.weather.ui.screens.services.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

internal class WeatherServicesStoreFactory(
    private val factory: StoreFactory,
) {

    fun create(): WeatherServicesStore =
        object : WeatherServicesStore,
            Store<WeatherServicesStore.Intent, WeatherServicesStore.State, WeatherServicesStore.Label> by factory.create(
                name = "WeatherServicesStore",
                initialState = WeatherServicesStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action

    private sealed interface Message

    private inner class ExecutorImpl :
        CoroutineExecutor<
            WeatherServicesStore.Intent,
            Action,
            WeatherServicesStore.State,
            Message,
            WeatherServicesStore.Label,
            >() {

        override fun executeIntent(intent: WeatherServicesStore.Intent) {
            when (intent) {
                is WeatherServicesStore.Intent.OnServiceClick ->
                    publish(label = WeatherServicesStore.Label.OpenDetailsLabel(service = intent.service))

                WeatherServicesStore.Intent.BackPressedIntent ->
                    publish(label = WeatherServicesStore.Label.FinishedLabel)
            }
        }

        override fun executeAction(action: Action) = Unit
    }

    private object ReducerImpl : Reducer<WeatherServicesStore.State, Message> {
        override fun WeatherServicesStore.State.reduce(msg: Message): WeatherServicesStore.State = this
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() = Unit
    }
}
