package io.pylyp.weather.domain.entity

import kotlin.math.abs

public fun temperatureDiscrepancyLevel(deltaC: Double): TemperatureDiscrepancyLevel {
    val a = abs(deltaC)
    return when {
        a < 2.0 -> TemperatureDiscrepancyLevel.LOW
        a <= 5.0 -> TemperatureDiscrepancyLevel.MEDIUM
        else -> TemperatureDiscrepancyLevel.HIGH
    }
}

public fun isHighTemperatureDiscrepancy(deltaC: Double): Boolean {
    return abs(deltaC) > 5.0
}
