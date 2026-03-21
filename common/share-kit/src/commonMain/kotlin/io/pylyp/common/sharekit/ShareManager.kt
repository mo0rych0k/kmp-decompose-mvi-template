package io.pylyp.common.sharekit

/**
 * Platform-specific manager to open the system share sheet with text content.
 */
public interface ShareManager {
    /**
     * Opens the system share sheet with the given content.
     *
     * @param content The text content to share.
     * @param mimeType The MIME type of the content (default: "text/plain").
     */
    public fun shareText(content: String, mimeType: String = "text/plain")
}
