package io.pylyp.common.core.ui.mapper

import io.pylyp.common.uikit.entity.UiError

public fun Throwable.toUiError(): UiError {
    return mapErrorPlatform(this)
}
