package io.pylyp.weather.domain.entity

import kotlin.math.floor

public enum class WindDirectionDD {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST,
}

/** Meteorological bearing to sector centre: 0° = from north, 45° per step clockwise. */
public fun WindDirectionDD.toCenterBearingDegrees(): Float = ordinal * 45f

/** Map a compass bearing (degrees clockwise from north) to the nearest 8-point direction. */
public fun Float.toWindDirectionDD(): WindDirectionDD {
    val normalized = ((this % 360f) + 360f) % 360f
    val index = floor((normalized + 22.5f) / 45f).toInt() % 8
    return WindDirectionDD.entries[index]
}
