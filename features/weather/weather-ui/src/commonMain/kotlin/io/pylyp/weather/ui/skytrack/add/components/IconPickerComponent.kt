package io.pylyp.weather.ui.skytrack.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.cloudinessTypes
import io.pylyp.weather.ui.skytrack.add.toWeatherIconRes
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import io.pylyp.weather.ui.skytrack.model.WeatherTypeUi
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun IconPickerComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    types: List<WeatherTypeUi>,
    selected: Set<WeatherTypeUi>,
    onToggle: (WeatherTypeUi) -> Unit,
    modifier: Modifier = Modifier,
    showInfoButton: Boolean = true,
    /** When false, only the leading icon and icon strip are shown (no section title text). */
    showSectionTitle: Boolean = true,
) {
    if (showSectionTitle) {
        TitleComponent(
            sectionTitle = sectionTitle,
            leadingIcon = leadingIcon,
            modifier = Modifier.fillMaxWidth(),
            showInfoButton = showInfoButton,
            titleTextStyle = MaterialTheme.typography.titleMedium,
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (!showSectionTitle) {
            leadingIcon()
        }
        val shape = MaterialTheme.shapes.medium
        for (type in types) {
            val isSelected = type in selected
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) AppColors.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = shape,
                    )
                    .background(
                        color = if (isSelected) AppColors.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                        shape = shape,
                    )
                    .clickable { onToggle(type) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(type.toWeatherIconRes()),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = if (isSelected) AppColors.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun IconPickerComponentPreview() {
    AppTheme {
        IconPickerComponent(
            sectionTitle = SectionTitleUi(
                title = "Cloudiness",
                infoSheetTitle = "Cloudiness",
                infoDescription = "Pick sky conditions.",
            ),
            leadingIcon = {},
            types = cloudinessTypes,
            selected = setOf(WeatherTypeUi.CLOUDY),
            onToggle = {},
        )
    }
}
