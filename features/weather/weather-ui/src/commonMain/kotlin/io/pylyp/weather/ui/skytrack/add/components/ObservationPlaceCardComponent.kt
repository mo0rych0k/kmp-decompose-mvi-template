package io.pylyp.weather.ui.skytrack.add.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.components.AddObservationLocationBlockComponent
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.model.GeoCoordinatesUi
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi

@Composable
internal fun ObservationPlaceCardComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    loadError: String?,
    isLoadingBackground: Boolean,
    coordinates: GeoCoordinatesUi?,
    locationLabel: String?,
    loadingText: String,
    modifier: Modifier = Modifier,
    showInfoButton: Boolean = true,
) {
    TitleComponent(
        sectionTitle = sectionTitle,
        leadingIcon = leadingIcon,
        modifier = Modifier.fillMaxWidth(),
        showInfoButton = showInfoButton,
        titleTextStyle = MaterialTheme.typography.titleMedium,
    )
    Card(
        colors = CardDefaults.cardColors(containerColor = AppColors.primary),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .heightIn(min = 48.dp),
        ) {
            when {
                loadError != null -> Text(
                    text = loadError,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall,
                )

                else -> AddObservationLocationBlockComponent(
                    isLoadingBackground = isLoadingBackground,
                    coordinates = coordinates,
                    locationLabel = locationLabel,
                    color = MaterialTheme.colorScheme.onPrimary,
                    loadingText = loadingText,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun ObservationPlaceCardComponentPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ObservationPlaceCardComponent(
                sectionTitle = SectionTitleUi(
                    title = "Place",
                    infoSheetTitle = "Place",
                    infoDescription = "Where the observation was taken.",
                ),
                leadingIcon = {},
                loadError = null,
                isLoadingBackground = false,
                coordinates = GeoCoordinatesUi(latitude = 50.0, longitude = 30.0),
                locationLabel = "Kyiv",
                loadingText = "Loading…",
            )
        }
    }
}
