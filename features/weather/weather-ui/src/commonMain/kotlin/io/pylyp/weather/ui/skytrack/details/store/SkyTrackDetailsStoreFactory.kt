package io.pylyp.weather.ui.skytrack.details.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.common.core.foundation.entity.Resource
import io.pylyp.common.sharekit.ShareManager
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.usecase.DeleteWeatherObservationUseCase
import io.pylyp.weather.domain.usecase.GetWeatherObservationByIdUseCase
import io.pylyp.weather.ui.share.toShareJson
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.toWeatherObservationRecordUi
import kotlinx.coroutines.launch

internal class SkyTrackDetailsStoreFactory(
    private val factory: StoreFactory,
    private val getByIdUseCase: GetWeatherObservationByIdUseCase,
    private val deleteUseCase: DeleteWeatherObservationUseCase,
    private val shareManager: ShareManager,
    private val recordId: Long,
) {

    fun create(): SkyTrackDetailsStore =
        object : SkyTrackDetailsStore,
            Store<SkyTrackDetailsStore.Intent, SkyTrackDetailsStore.State, SkyTrackDetailsStore.Label> by factory.create(
                name = "SkyTrackDetailsStore",
                initialState = SkyTrackDetailsStore.State(recordId = recordId),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data object LoadAction : Action
    }

    private sealed interface Message {
        data class LoadedMessage(val record: WeatherObservationRecordUi?) : Message
        data class ErrorMessage(val message: String) : Message
        data class LoadingMessage(val isLoading: Boolean) : Message
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.LoadAction)
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<
            SkyTrackDetailsStore.Intent,
            Action,
            SkyTrackDetailsStore.State,
            Message,
            SkyTrackDetailsStore.Label,
            >() {

        override fun executeIntent(intent: SkyTrackDetailsStore.Intent) {
            when (intent) {
                SkyTrackDetailsStore.Intent.BackIntent -> publish(SkyTrackDetailsStore.Label.BackLabel)
                SkyTrackDetailsStore.Intent.RetryIntent -> forward(Action.LoadAction)
                SkyTrackDetailsStore.Intent.DeleteIntent -> delete()
                SkyTrackDetailsStore.Intent.ShareIntent -> share()
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.LoadAction -> load()
            }
        }

        private fun load() {
            scope.launch {
                getByIdUseCase(recordId).collect { resource: Resource<WeatherObservationRecordDD?> ->
                    when (resource) {
                        Resource.Idle -> Unit
                        Resource.Loading -> dispatch(Message.LoadingMessage(isLoading = true))
                        is Resource.Success -> {
                            dispatch(Message.LoadingMessage(isLoading = false))
                            dispatch(
                                Message.LoadedMessage(
                                    record = resource.data?.toWeatherObservationRecordUi(),
                                ),
                            )
                        }

                        is Resource.Error -> {
                            dispatch(Message.LoadingMessage(isLoading = false))
                            dispatch(Message.ErrorMessage(message = resource.message))
                        }
                    }
                }
            }
        }

        private fun delete() {
            scope.launch {
                deleteUseCase(recordId).collect { resource: Resource<Unit> ->
                    when (resource) {
                        Resource.Idle -> Unit
                        Resource.Loading -> dispatch(Message.LoadingMessage(isLoading = true))
                        is Resource.Success -> publish(SkyTrackDetailsStore.Label.DeletedLabel)
                        is Resource.Error -> {
                            dispatch(Message.LoadingMessage(isLoading = false))
                            dispatch(Message.ErrorMessage(message = resource.message))
                        }
                    }
                }
            }
        }

        private fun share() {
            val rec = state().record ?: return
            shareManager.shareText(rec.toShareJson())
        }
    }

    private object ReducerImpl : Reducer<SkyTrackDetailsStore.State, Message> {
        override fun SkyTrackDetailsStore.State.reduce(msg: Message): SkyTrackDetailsStore.State {
            return when (msg) {
                is Message.LoadingMessage -> copy(isLoading = msg.isLoading)
                is Message.LoadedMessage -> copy(record = msg.record, errorMessage = null)
                is Message.ErrorMessage -> copy(errorMessage = msg.message)
            }
        }
    }
}
