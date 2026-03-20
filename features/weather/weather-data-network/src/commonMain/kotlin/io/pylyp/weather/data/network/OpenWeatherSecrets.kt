package io.pylyp.weather.data.network

/** Provide API key per platform (e.g. env on JVM, plist on iOS, BuildConfig on Android). */
internal expect fun openWeatherApiKey(): String
