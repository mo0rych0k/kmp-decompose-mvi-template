package io.pylyp.weather.data.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MetNoForecastCompactND(
    val properties: MetNoForecastPropertiesND,
)

@Serializable
public data class MetNoForecastPropertiesND(
    val timeseries: List<MetNoTimeSeriesEntryND> = emptyList(),
)

@Serializable
public data class MetNoTimeSeriesEntryND(
    val time: String? = null,
    val data: MetNoTimeSeriesDataND? = null,
)

@Serializable
public data class MetNoTimeSeriesDataND(
    val instant: MetNoInstantND? = null,
    @SerialName("next_1_hours") val next1Hours: MetNoNext1HoursND? = null,
)

@Serializable
public data class MetNoInstantND(
    val details: MetNoInstantDetailsND? = null,
)

@Serializable
public data class MetNoInstantDetailsND(
    @SerialName("air_temperature") val airTemperature: Double? = null,
    @SerialName("wind_speed") val windSpeed: Double? = null,
)

@Serializable
public data class MetNoNext1HoursND(
    val summary: MetNoSummaryND? = null,
)

@Serializable
public data class MetNoSummaryND(
    @SerialName("symbol_code") val symbolCode: String? = null,
)
