package io.pylyp.coffee.ui.screens.gallery.entity

import androidx.compose.runtime.Immutable
import io.pylyp.common.uikit.entity.PrintableText

@Immutable
internal data class DialogUiData(
    val title: PrintableText,
    val description: PrintableText?,
    val buttonsPositiveText: PrintableText,
    val buttonsPositiveAction: Action,
    val buttonNegativeText: PrintableText?,
    val buttonNegativeAction: Action?,
) {
    enum class Action {
        Retry,
        Close,
    }
}