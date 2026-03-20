package io.pylyp.weather.data.network

import platform.Foundation.NSBundle
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
internal actual fun openWeatherApiKey(): String {
    val key = NSBundle.mainBundle.objectForInfoDictionaryKey("OPENWEATHER_API_KEY") as? String
    require(!key.isNullOrBlank()) {
        "OPENWEATHER_API_KEY is missing or blank in iosApp/iosApp/Info.plist"
    }
    return key
}
