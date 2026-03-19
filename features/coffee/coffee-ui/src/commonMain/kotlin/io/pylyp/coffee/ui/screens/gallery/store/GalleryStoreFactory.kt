package io.pylyp.coffee.ui.screens.gallery.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import io.pylyp.coffee.domain.usecase.CoffeeListFlowUseCase
import io.pylyp.coffee.domain.usecase.FetchNewPageUseCase
import io.pylyp.coffee.ui.screens.gallery.entity.CoffeeImageUiData
import io.pylyp.coffee.ui.screens.gallery.entity.DialogUiData
import io.pylyp.coffee.ui.screens.gallery.mapper.toUi
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStore.Label.OpenDetailsLabel
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStoreFactory.Message.CoffeeListLoadedMessage
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStoreFactory.Message.IsLoadingMessage
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStoreFactory.Message.SelectVisibleItemMessage
import io.pylyp.common.core.foundation.entity.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class GalleryStoreFactory(
    private val factory: StoreFactory,
    private val coffeeListFlowUseCase: CoffeeListFlowUseCase,
    private val fetchNewPageUseCase: FetchNewPageUseCase,
) {

    fun create(): GalleryStore =
        object : GalleryStore,
            Store<GalleryStore.Intent, GalleryStore.State, GalleryStore.Label> by factory.create(
                name = "GalleryStore",
                initialState = GalleryStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data class OnClickImageAction(val index: Int) : Action
        data class CoffeeListLoadedAction(val images: List<CoffeeImageUiData>) : Action
        data object FetchNewPageAction : Action

    }

    private sealed interface Message {
        data class DialogMessage(val dialog: DialogUiData?) : Message
        data class IsLoadingMessage(val isLoading: Boolean) : Message
        data class SelectVisibleItemMessage(val index: Int) : Message
        data class CoffeeListLoadedMessage(val images: List<CoffeeImageUiData>) : Message
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<GalleryStore.Intent, Action, GalleryStore.State,
                Message, GalleryStore.Label>() {
        private var jobFetchNewPageUseCase: Job? = null

        override fun executeIntent(intent: GalleryStore.Intent) {
            when (intent) {
                is GalleryStore.Intent.OnPressedImageIntent ->
                    publish(label = OpenDetailsLabel(index = intent.index))

                is GalleryStore.Intent.SetVisibleItemIntent ->
                    dispatch(message = SelectVisibleItemMessage(index = intent.index))

                is GalleryStore.Intent.CloseDialogIntent ->
                    dispatch(message = Message.DialogMessage(dialog = null))

                is GalleryStore.Intent.CloseScreenIntent ->
                    publish(label = GalleryStore.Label.OnBackLabel)

                is GalleryStore.Intent.LoadNewPageIntent -> fetchNewPage()
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.OnClickImageAction ->
                    publish(label = OpenDetailsLabel(index = action.index))

                is Action.CoffeeListLoadedAction ->
                    dispatch(message = CoffeeListLoadedMessage(images = action.images))

                is Action.FetchNewPageAction -> fetchNewPage()
            }
        }

        private fun fetchNewPage() {
            if (jobFetchNewPageUseCase?.isActive == true) return

            jobFetchNewPageUseCase?.cancel()

            val job = fetchNewPageUseCase(Unit)
                .onEach { resource: Resource<Unit> ->
                    dispatch(message = IsLoadingMessage(isLoading = resource is Resource.Loading))
                }
                .launchIn(scope)

            jobFetchNewPageUseCase = job
        }
    }

    private object ReducerImpl : Reducer<GalleryStore.State, Message> {
        override fun GalleryStore.State.reduce(msg: Message): GalleryStore.State {
            return when (msg) {
                is CoffeeListLoadedMessage -> {
                    val isLoading = if (msg.images.isNotEmpty()) {
                        false
                    } else {
                        this.isLoading
                    }

                    copy(
                        images = msg.images,
                        isLoading = isLoading
                    )
                }

                is SelectVisibleItemMessage -> copy(showedImageIndex = msg.index)
                is IsLoadingMessage -> copy(isLoading = msg.isLoading)
                is Message.DialogMessage -> copy(dialogUiData = msg.dialog)
            }
        }
    }

    private inner class BootstrapperImpl :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            coffeeListFlowUseCase()
                .onEach { images ->
                    dispatch(Action.CoffeeListLoadedAction(images = images.map { it.toUi() }))
                }
                .launchIn(scope)

            dispatch(Action.FetchNewPageAction)
        }
    }
}
