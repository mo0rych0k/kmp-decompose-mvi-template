package io.pylyp.weather.ui.skytrack

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import io.pylyp.weather.domain.entity.GeoCoordinatesDD
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt

/** ~100 m precision for display (two decimals). */
internal fun formatShortCoordinates(latitude: Double?, longitude: Double?): String {
    if (latitude == null || longitude == null) return "—"
    return "${formatCoord(latitude)}°, ${formatCoord(longitude)}°"
}

internal fun formatShortCoordinates(coords: GeoCoordinatesDD): String =
    formatShortCoordinates(coords.latitude, coords.longitude)

private fun formatCoord(value: Double): String {
    val sign = if (value < 0) "-" else ""
    val abs = abs(value)
    val rounded = round(abs * 100.0) / 100.0
    val intPart = rounded.toInt()
    val frac = ((rounded - intPart) * 100).roundToInt().toString().padStart(2, '0')
    return "$sign$intPart.$frac"
}

@Composable
internal fun ObservationLocationBlock(
    record: WeatherObservationRecordDD,
    color: Color,
    unknownLabel: String,
    modifier: Modifier = Modifier,
) {
    val label = record.locationLabel?.takeIf { it.isNotBlank() }
    val coords = formatShortCoordinates(record.latitude, record.longitude)
    when {
        label != null -> Column(modifier = modifier) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = color,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = coords,
                style = MaterialTheme.typography.bodySmall,
                color = color.copy(alpha = 0.85f),
            )
        }

        record.latitude != null -> Text(
            text = coords,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            modifier = modifier,
        )

        else -> Text(
            text = unknownLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            modifier = modifier,
        )
    }
}

@Composable
internal fun AddObservationLocationBlock(
    isLoadingBackground: Boolean,
    coordinates: GeoCoordinatesDD?,
    locationLabel: String?,
    color: Color,
    loadingText: String,
) {
    when {
        isLoadingBackground -> Text(
            text = loadingText,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Black,
            color = color,
        )

        coordinates == null -> Unit
        else -> Column {
            locationLabel?.takeIf { it.isNotBlank() }?.let { place ->
                Text(
                    text = place,
                    style = MaterialTheme.typography.bodyMedium,
                    color = color.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = formatShortCoordinates(coordinates),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = color,
            )
        }
    }
}
