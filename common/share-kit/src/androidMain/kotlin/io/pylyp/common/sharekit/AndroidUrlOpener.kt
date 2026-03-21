package io.pylyp.common.sharekit

import android.content.Context
import android.content.Intent
import android.net.Uri

internal class AndroidUrlOpener(
    private val context: Context,
) : UrlOpener {
    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            // No handler for URL
        }
    }
}
