package io.pylyp.common.core.ui.mapper

import io.pylyp.common.resources.internal.Res
import io.pylyp.common.resources.internal.error_generic_message
import io.pylyp.common.resources.internal.error_generic_title
import io.pylyp.common.resources.internal.error_serialization_message
import io.pylyp.common.resources.internal.error_serialization_title
import io.pylyp.common.uikit.entity.UiError
import io.pylyp.common.uikit.entity.rawPrintableText
import io.pylyp.common.uikit.entity.resPrintableText
import kotlinx.coroutines.CancellationException

/**
 * Handles error types that are common across all platforms:
 * - [CancellationException] — should never surface in UI; rethrows
 * - Serialization errors matched by class name — avoids depending on kotlinx.serialization directly
 * - Everything else → generic "unknown error" UiError
 */
internal fun mapCommonError(error: Throwable): UiError {
    // Coroutine cancellations must always propagate — never swallow them
    if (error is CancellationException) throw error

    // Match kotlinx.serialization exceptions by class name to avoid a direct dependency
    val className = error::class.qualifiedName.orEmpty()
    if (className.startsWith("kotlinx.serialization")) {
        return UiError(
            title = resPrintableText(Res.string.error_serialization_title),
            description = resPrintableText(Res.string.error_serialization_message),
            image = null,
            cause = error,
        )
    }

    // Generic fallback
    return UiError(
        title = resPrintableText(Res.string.error_generic_title),
        description = error.getPlatformErrorMessage()
            ?.takeIf { it.isNotBlank() }
            ?.let { rawPrintableText(it) }
            ?: resPrintableText(Res.string.error_generic_message),
        image = null,
        cause = error,
    )
}
