package io.pylyp.weather.ui.skytrack.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class SectionTitleUi(
    val title: String,
    val infoSheetTitle: String,
    val infoDescription: String,
)
