package io.pylyp.common.core.ui.mapper

import io.pylyp.common.uikit.entity.UiError

internal expect fun mapErrorPlatform(error: Throwable): UiError
