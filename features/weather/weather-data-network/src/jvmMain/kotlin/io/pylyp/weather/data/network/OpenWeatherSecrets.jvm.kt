package io.pylyp.weather.data.network

internal actual fun openWeatherApiKey(): String {
    return System.getenv("OPENWEATHER_API_KEY").orEmpty()
}
