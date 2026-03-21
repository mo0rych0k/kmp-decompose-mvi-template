package io.pylyp.common.sharekit

import java.net.URI

internal class JvmUrlOpener : UrlOpener {
    override fun openUrl(url: String) {
        try {
            val desktop = java.awt.Desktop.getDesktop()
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                desktop.browse(URI.create(url))
            }
        } catch (_: Exception) {
            // Browse not supported or failed
        }
    }
}
