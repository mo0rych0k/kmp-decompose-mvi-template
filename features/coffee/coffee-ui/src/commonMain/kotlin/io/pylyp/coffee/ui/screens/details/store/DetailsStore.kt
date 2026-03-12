package io.pylyp.coffee.ui.screens.details.store

import com.arkivanov.mvikotlin.core.store.Store
import io.pylyp.coffee.ui.screens.gallery.entity.CoffeeImageUiData

internal interface DetailsStore :
    Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {
    sealed interface Intent {
        data class OnPageChanged(val index: Int) : Intent
    }

    data class State(
        val images: List<CoffeeImageUiData> = emptyList(),
        val showedImageIndex: Int? = null,
        val currentIndexPage: Int? = null,
    )

    sealed interface Label {}
}