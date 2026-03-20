package io.pylyp.weather.domain.location

/**
 * Resolves a human-readable place label (locality / area) from coordinates, e.g. via reverse geocoding.
 */
public interface PlaceLabelProvider {
    public suspend fun resolveLabel(latitude: Double, longitude: Double): String?
}
