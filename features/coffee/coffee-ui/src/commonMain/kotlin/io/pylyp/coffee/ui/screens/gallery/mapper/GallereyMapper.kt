package io.pylyp.coffee.ui.screens.gallery.mapper

import io.pylyp.coffee.domain.entity.CoffeeImageDD
import io.pylyp.coffee.ui.screens.gallery.entity.CoffeeImageUiData

internal fun CoffeeImageDD.toUi(): CoffeeImageUiData {
    return CoffeeImageUiData(
        id = id,
        url = url,
    )
}