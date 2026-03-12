package io.pylyp.coffee.ui.screens.gallery.entity

import androidx.compose.runtime.Immutable

@Immutable
public data class CoffeeImageUiData(
    val id: Long,
    val url: String,
)