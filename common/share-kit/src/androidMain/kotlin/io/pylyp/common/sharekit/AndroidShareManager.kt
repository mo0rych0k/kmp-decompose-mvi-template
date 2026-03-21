package io.pylyp.common.sharekit

import android.content.Context
import android.content.Intent

internal class AndroidShareManager(
    private val context: Context,
) : ShareManager {
    override fun shareText(content: String, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_TEXT, content)
        }
        val chooser = Intent.createChooser(intent, null)
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}
