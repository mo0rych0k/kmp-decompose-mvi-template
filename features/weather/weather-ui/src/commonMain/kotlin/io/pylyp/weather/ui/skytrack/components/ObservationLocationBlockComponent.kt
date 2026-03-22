package io.pylyp.weather.ui.skytrack.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.model.GeoCoordinatesUi
import io.pylyp.weather.ui.skytrack.model.ObservationLocationUi
import io.pylyp.weather.ui.skytrack.model.formatCoordinatesDisplay

@Composable
internal fun ObservationLocationBlockComponent(
    location: ObservationLocationUi,
    unknownLabel: String,
    color: Color,
    modifier: Modifier = Modifier,
    showCoordinates: Boolean = true,
    /** When null, [MaterialTheme.typography.bodyMedium] is used. */
    placeLabelStyle: TextStyle? = null,
) {
    val placeStyle = placeLabelStyle ?: MaterialTheme.typography.bodyMedium
    when (location) {
        is ObservationLocationUi.WithPlace ->
            Column(modifier = modifier) {
                Text(
                    text = location.placeLabel,
                    style = placeStyle,
                    color = color,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (showCoordinates) {
                    Text(
                        text = location.coordinatesLine,
                        style = MaterialTheme.typography.bodySmall,
                        color = color.copy(alpha = 0.85f),
                    )
                }
            }

        is ObservationLocationUi.CoordinatesOnly ->
            Text(
                text = if (showCoordinates) location.coordinatesLine else "—",
                style = placeStyle,
                color = color,
                modifier = modifier,
            )

        ObservationLocationUi.Unknown ->
            Text(
                text = unknownLabel,
                style = placeStyle,
                color = color,
                modifier = modifier,
            )
    }
}

@Composable
internal fun AddObservationLocationBlockComponent(
    isLoadingBackground: Boolean,
    coordinates: GeoCoordinatesUi?,
    locationLabel: String?,
    color: Color,
    loadingText: String,
) {
    when {
        isLoadingBackground ->
            Text(
                text = loadingText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = color,
            )

        coordinates == null -> Unit
        else ->
            Column {
                locationLabel?.takeIf { it.isNotBlank() }?.let { place ->
                    Text(
                        text = place,
                        style = MaterialTheme.typography.bodySmall,
                        color = color.copy(alpha = 0.9f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = formatCoordinatesDisplay(coordinates.latitude, coordinates.longitude),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = color.copy(alpha = 0.9f),
                )
            }
    }
}

@Preview
@Composable
internal fun ObservationLocationBlockComponentPreview() {
    AppTheme {
        ObservationLocationBlockComponent(
            location = ObservationLocationUi.WithPlace(
                placeLabel = "Kyiv",
                coordinatesLine = "50.45°, 30.52°",
            ),
            unknownLabel = "—",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
internal fun AddObservationLocationBlockComponentPreview() {
    AppTheme {
        AddObservationLocationBlockComponent(
            isLoadingBackground = false,
            coordinates = GeoCoordinatesUi(50.45, 30.52),
            locationLabel = "Kyiv",
            color = MaterialTheme.colorScheme.onPrimary,
            loadingText = "…",
        )
    }
}
