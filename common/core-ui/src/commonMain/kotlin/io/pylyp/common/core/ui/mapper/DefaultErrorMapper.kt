package io.pylyp.common.core.ui.mapper

import io.pylyp.common.uikit.entity.UiError

public object DefaultErrorMapper {

    public fun mapError(error: Throwable): UiError {
        return mapErrorPlatform(error)
    }

}
