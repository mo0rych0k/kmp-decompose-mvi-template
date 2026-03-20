package io.pylyp.weather.domain.entity

import kotlin.math.round

public data class GeoCoordinatesDD(
    public val latitude: Double,
    public val longitude: Double,
)

/** Rounds to ~11 m precision for storage and API — avoids unnecessary GPS noise. */
public fun GeoCoordinatesDD.roundedForStorage(): GeoCoordinatesDD {
    val p = 10_000.0
    return GeoCoordinatesDD(
        latitude = round(latitude * p) / p,
        longitude = round(longitude * p) / p,
    )
}
