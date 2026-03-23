package io.pylyp.common.sharekit

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

internal class JvmShareManager : ShareManager {
    override fun shareText(content: String, mimeType: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(content), null)
    }
}
