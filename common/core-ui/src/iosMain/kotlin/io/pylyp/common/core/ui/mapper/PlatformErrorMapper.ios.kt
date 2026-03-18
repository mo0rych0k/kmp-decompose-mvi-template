package io.pylyp.common.core.ui.mapper

import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.pylyp.common.resources.internal.Res
import io.pylyp.common.resources.internal.error_network_message
import io.pylyp.common.resources.internal.error_network_title
import io.pylyp.common.resources.internal.error_ssl_message
import io.pylyp.common.resources.internal.error_ssl_title
import io.pylyp.common.resources.internal.error_timeout_message
import io.pylyp.common.resources.internal.error_timeout_title
import io.pylyp.common.uikit.entity.UiError
import io.pylyp.common.uikit.entity.resPrintableText
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorCannotConnectToHost
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorSecureConnectionFailed
import platform.Foundation.NSURLErrorServerCertificateUntrusted
import platform.Foundation.NSURLErrorTimedOut

internal actual fun mapErrorPlatform(error: Throwable): UiError {
    // Walk the cause chain to find a wrapped NSError from the Kotlin/Native ObjC bridge.
    // Ktor's Darwin engine wraps NSError in an ObjcException that exposes an `nsError` property.
    var current: Throwable? = error
    while (current != null) {
        if (current is DarwinHttpRequestException) {
            val nsError = current.origin
            if (nsError.domain == NSURLErrorDomain) {
                return nsError.toUiError(originalCause = error)
            }
        }
        current = current.cause
    }

    return mapCommonError(error)
}

private fun NSError.toUiError(originalCause: Throwable): UiError = when (code) {
    NSURLErrorTimedOut -> UiError(
        title = resPrintableText(Res.string.error_timeout_title),
        description = resPrintableText(Res.string.error_timeout_message),
        image = null,
        cause = originalCause,
    )

    NSURLErrorNotConnectedToInternet,
    NSURLErrorCannotFindHost,
    NSURLErrorCannotConnectToHost,
    NSURLErrorNetworkConnectionLost,
        -> UiError(
        title = resPrintableText(Res.string.error_network_title),
        description = resPrintableText(Res.string.error_network_message),
        image = null,
        cause = originalCause,
    )

    NSURLErrorSecureConnectionFailed,
    NSURLErrorServerCertificateUntrusted,
        -> UiError(
        title = resPrintableText(Res.string.error_ssl_title),
        description = resPrintableText(Res.string.error_ssl_message),
        image = null,
        cause = originalCause,
    )

    else -> UiError(
        title = resPrintableText(Res.string.error_network_title),
        description = resPrintableText(Res.string.error_network_message),
        image = null,
        cause = originalCause,
    )
}
