package io.pylyp.weather.domain.entity

public data class SkyTrackBackgroundWeather(
    public val coordinates: GeoCoordinatesDD,
    public val apiWeather: CommonWeatherDD,
)
