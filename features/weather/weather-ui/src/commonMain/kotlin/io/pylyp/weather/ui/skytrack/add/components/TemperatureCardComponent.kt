package io.pylyp.weather.ui.skytrack.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.store.TemperatureUnit
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import io.pylyp.weather.ui.skytrack.temperatureSliderAccentColor

@Composable
internal fun TemperatureCardComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    temperatureUnit: TemperatureUnit,
    userTemperatureC: Double,
    unitCelsiusLabel: String,
    unitFahrenheitLabel: String,
    onTemperatureChange: (Double) -> Unit,
    onUnitToggle: () -> Unit,
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
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val isLightTheme = !isSystemInDarkTheme()
            val displayValue = when (temperatureUnit) {
                TemperatureUnit.CELSIUS -> userTemperatureC.toInt()
                TemperatureUnit.FAHRENHEIT -> (userTemperatureC * 9 / 5 + 32).toInt()
            }
            val displayUnit = when (temperatureUnit) {
                TemperatureUnit.CELSIUS -> unitCelsiusLabel
                TemperatureUnit.FAHRENHEIT -> unitFahrenheitLabel
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$displayValue$displayUnit",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = temperatureSliderAccentColor(userTemperatureC, isLightTheme = isLightTheme),
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    TemperatureUnit.entries.forEach { unit ->
                        val isSelected = temperatureUnit == unit
                        val label = when (unit) {
                            TemperatureUnit.CELSIUS -> unitCelsiusLabel
                            TemperatureUnit.FAHRENHEIT -> unitFahrenheitLabel
                        }
                        Box(
                            modifier = Modifier
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) AppColors.primary else MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.5f,
                                    ),
                                    shape = MaterialTheme.shapes.small,
                                )
                                .background(
                                    color = if (isSelected) AppColors.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.5f,
                                    ),
                                    shape = MaterialTheme.shapes.small,
                                )
                                .clickable {
                                    if (!isSelected) {
                                        onUnitToggle()
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AppColors.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
            Slider(
                value = userTemperatureC.toFloat(),
                onValueChange = { onTemperatureChange(it.toDouble()) },
                valueRange = -30f..45f,
                colors = SliderDefaults.colors(
                    thumbColor = temperatureSliderAccentColor(userTemperatureC, isLightTheme = isLightTheme),
                    activeTrackColor = temperatureSliderAccentColor(userTemperatureC, isLightTheme = isLightTheme),
                    inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                ),
            )
        }
    }
}

@Preview
@Composable
internal fun TemperatureCardComponentPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            TemperatureCardComponent(
                sectionTitle = SectionTitleUi(
                    title = "Temperature",
                    infoSheetTitle = "Temperature",
                    infoDescription = "Set air temperature.",
                ),
                leadingIcon = {},
                temperatureUnit = TemperatureUnit.CELSIUS,
                userTemperatureC = 12.0,
                unitCelsiusLabel = "°C",
                unitFahrenheitLabel = "°F",
                onTemperatureChange = {},
                onUnitToggle = {},
            )
        }
    }
}
