package io.pylyp.cover.ui.screens.cover.store

import com.arkivanov.mvikotlin.core.store.Store

internal interface CoverStore : Store<CoverStore.Intent, CoverStore.State, CoverStore.Label> {
    sealed interface Intent {
        data object BackPressedIntent : Intent
        data object OnNavigateToCoffeeIntent : Intent
        data object OnNavigateToSkyTrackIntent : Intent
    }

    data class State(
        val isLoading: Boolean = false,
    )

    sealed interface Label {
        data object BackPressedLabel : Label
        data object OnNavigateToCoffeeLabel : Label
        data object OnNavigateToSkyTrackLabel : Label
    }
}