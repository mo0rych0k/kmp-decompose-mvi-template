package io.pylyp.common.sharekit

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal class IosUrlOpener : UrlOpener {
    override fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}
