package io.pylyp.weather.ui.skytrack.model

/** User-facing temperature, e.g. `+5°` (no service/API line). */
internal fun formatTemperatureDegreesDisplay(value: Double): String {
    val sign = if (value >= 0) "+" else ""
    return "$sign${value.toInt()}°"
}
