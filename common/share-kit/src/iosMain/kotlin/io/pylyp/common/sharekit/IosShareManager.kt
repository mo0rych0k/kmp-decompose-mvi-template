package io.pylyp.common.sharekit

import platform.Foundation.NSString
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.popoverPresentationController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlinx.cinterop.ExperimentalForeignApi

internal class IosShareManager : ShareManager {
    @OptIn(ExperimentalForeignApi::class)
    override fun shareText(content: String, mimeType: String) {
        dispatch_async(dispatch_get_main_queue()) {
            val items = listOf(content as NSString)

            val activityVC = UIActivityViewController(
                activityItems = items,
                applicationActivities = null,
            )

            val window = UIApplication.sharedApplication.windows.firstOrNull() as? platform.UIKit.UIWindow
            val rootVC = window?.rootViewController

            activityVC.popoverPresentationController?.sourceView = rootVC?.view
            activityVC.popoverPresentationController?.sourceRect =
                platform.CoreGraphics.CGRectMake(0.0, 0.0, 0.0, 0.0)

            rootVC?.presentViewController(activityVC, animated = true, completion = null)
        }
    }
}
