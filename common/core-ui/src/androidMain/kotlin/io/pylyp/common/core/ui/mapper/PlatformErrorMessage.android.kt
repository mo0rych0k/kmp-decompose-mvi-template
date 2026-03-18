package io.pylyp.common.core.ui.mapper

internal actual fun Throwable.getPlatformErrorMessage(): String? = this.localizedMessage
