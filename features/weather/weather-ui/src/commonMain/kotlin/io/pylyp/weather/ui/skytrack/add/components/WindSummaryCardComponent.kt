package io.pylyp.weather.ui.skytrack.add.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.wind.windDirectionDisplayName
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.components.WindDirectionArrowIconComponent
import io.pylyp.weather.ui.skytrack.components.WindStrengthIconsComponent
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import io.pylyp.weather.ui.skytrack.model.WindDirectionUi

@Composable
internal fun WindSummaryCardComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    windDirectionLabel: String,
    windStrengthLabel: String,
    userWindStrengthPercent: Int,
    userWindDirection: WindDirectionUi,
    windDirectionDegrees: Float,
    openWindSetupLabel: String,
    onOpenWindSetup: () -> Unit,
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
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenWindSetup),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .heightIn(min = 72.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            val labelStyle = MaterialTheme.typography.labelMedium
            val valueStyle = MaterialTheme.typography.bodyMedium
            val isZeroState = userWindStrengthPercent == 0

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = windDirectionLabel,
                    style = labelStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (!isZeroState) {
                    WindDirectionArrowIconComponent(
                        rotationDegrees = windDirectionDegrees,
                        contentDescription = null,
                        iconSize = 20.dp,
                        tint = AppColors.primary,
                    )
                }
                Text(
                    text = if (isZeroState) "—" else windDirectionDisplayName(userWindDirection),
                    style = valueStyle,
                    fontWeight = FontWeight.Bold,
                    color = if (isZeroState) MaterialTheme.colorScheme.onSurfaceVariant else AppColors.primary,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = windStrengthLabel,
                    style = labelStyle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (isZeroState) {
                    Text(
                        text = "—",
                        style = valueStyle,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    WindStrengthIconsComponent(
                        percent = userWindStrengthPercent,
                        tint = AppColors.primary,
                        iconSize = 16.dp,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Text(
                text = openWindSetupLabel,
                style = MaterialTheme.typography.labelMedium,
                color = AppColors.primary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Preview
@Composable
internal fun WindSummaryCardComponentPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            WindSummaryCardComponent(
                sectionTitle = SectionTitleUi(
                    title = "Wind",
                    infoSheetTitle = "Wind",
                    infoDescription = "Direction and strength.",
                ),
                leadingIcon = {},
                windDirectionLabel = "Wind direction",
                windStrengthLabel = "Wind strength",
                userWindStrengthPercent = 50,
                userWindDirection = WindDirectionUi.NORTH_EAST,
                windDirectionDegrees = 45f,
                openWindSetupLabel = "Set wind…",
                onOpenWindSetup = {},
            )
            WindSummaryCardComponent(
                sectionTitle = SectionTitleUi(
                    title = "Wind",
                    infoSheetTitle = "Wind",
                    infoDescription = "Direction and strength.",
                ),
                leadingIcon = {},
                windDirectionLabel = "Wind direction",
                windStrengthLabel = "Wind strength",
                userWindStrengthPercent = 0,
                userWindDirection = WindDirectionUi.NORTH,
                windDirectionDegrees = 0f,
                openWindSetupLabel = "Set wind…",
                onOpenWindSetup = {},
            )
        }
    }
}
