package io.pylyp.common.core.ui.mapper

import io.pylyp.common.resources.internal.Res
import io.pylyp.common.resources.internal.error_network_message
import io.pylyp.common.resources.internal.error_network_title
import io.pylyp.common.resources.internal.error_ssl_message
import io.pylyp.common.resources.internal.error_ssl_title
import io.pylyp.common.resources.internal.error_timeout_message
import io.pylyp.common.resources.internal.error_timeout_title
import io.pylyp.common.uikit.entity.UiError
import io.pylyp.common.uikit.entity.resPrintableText
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

internal actual fun mapErrorPlatform(error: Throwable): UiError {
    return when (error) {
        is SocketTimeoutException -> UiError(
            title = resPrintableText(Res.string.error_timeout_title),
            description = resPrintableText(Res.string.error_timeout_message),
            image = null,
            cause = error,
        )

        is UnknownHostException -> UiError(
            title = resPrintableText(Res.string.error_network_title),
            description = resPrintableText(Res.string.error_network_message),
            image = null,
            cause = error,
        )

        is SSLException -> UiError(
            title = resPrintableText(Res.string.error_ssl_title),
            description = resPrintableText(Res.string.error_ssl_message),
            image = null,
            cause = error,
        )

        is IOException -> UiError(
            title = resPrintableText(Res.string.error_network_title),
            description = resPrintableText(Res.string.error_network_message),
            image = null,
            cause = error,
        )

        else -> mapCommonError(error)
    }
}
