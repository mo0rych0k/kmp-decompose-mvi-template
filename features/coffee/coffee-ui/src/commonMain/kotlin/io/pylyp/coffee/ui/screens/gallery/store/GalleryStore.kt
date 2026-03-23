package io.pylyp.coffee.ui.screens.gallery.store

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.coffee.ui.screens.gallery.entity.CoffeeImageUiData
import io.pylyp.coffee.ui.screens.gallery.entity.DialogUiData

internal interface GalleryStore :
    Store<GalleryStore.Intent, GalleryStore.State, GalleryStore.Label> {
    sealed interface Intent {
        data class OnPressedImageIntent(val index: Int) : Intent
        data class SetVisibleItemIntent(val index: Int) : Intent
        data object LoadNewPageIntent : Intent
        data object CloseScreenIntent : Intent
        data object CloseDialogIntent : Intent
    }

    @Immutable
    data class State(
        val isLoading: Boolean = false,
        val images: List<CoffeeImageUiData> = emptyList(),
        val showedImageIndex: Int? = null,
        val dialogUiData: DialogUiData? = null,
    )

    sealed interface Label {
        data class OpenDetailsLabel(val index: Int) : Label
        data object OnBackLabel : Label
    }
}