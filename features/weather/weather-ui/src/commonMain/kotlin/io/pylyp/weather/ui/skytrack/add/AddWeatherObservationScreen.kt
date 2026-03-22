package io.pylyp.weather.ui.skytrack.add

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.btn_save_observation
import io.pylyp.common.resources.header_loading
import io.pylyp.common.resources.label_place
import io.pylyp.common.resources.label_temperature
import io.pylyp.common.resources.label_weather
import io.pylyp.common.resources.btn_open_wind_setup
import io.pylyp.common.resources.label_precipitation
import io.pylyp.common.resources.label_wind
import io.pylyp.common.resources.observation_save_missing_background
import io.pylyp.common.resources.screen_new_observation_title
import io.pylyp.common.resources.unit_celsius
import io.pylyp.common.resources.unit_fahrenheit
import io.pylyp.common.resources.wind_add_data
import io.pylyp.common.resources.wind_still
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.ui.skytrack.AddObservationLocationBlock
import io.pylyp.weather.ui.skytrack.WindStrengthIcons
import io.pylyp.weather.ui.skytrack.add.wind.WindDirectionStrengthScreen
import io.pylyp.weather.ui.skytrack.add.wind.windDirectionDisplayName
import io.pylyp.weather.ui.skytrack.add.store.AddWeatherObservationStore
import io.pylyp.weather.ui.skytrack.add.store.SAVE_ERROR_MISSING_BACKGROUND_KEY
import io.pylyp.weather.ui.skytrack.add.store.TemperatureUnit
import io.pylyp.weather.ui.skytrack.temperatureSliderAccentColor
import io.pylyp.weather.ui.skytrack.add.cloudinessTypes
import io.pylyp.weather.ui.skytrack.add.precipitationSectionIconRes
import io.pylyp.weather.ui.skytrack.add.precipitationTypes
import io.pylyp.weather.ui.skytrack.add.temperatureIconRes
import io.pylyp.weather.ui.skytrack.add.toWeatherIconRes
import io.pylyp.weather.ui.skytrack.add.weatherSectionIconRes
import io.pylyp.weather.ui.skytrack.add.windSectionIconRes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddWeatherObservationScreen(
    component: AddWeatherObservationComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    if (state.isWindSetupVisible) {
        WindDirectionStrengthScreen(
            windDirectionDegrees = state.windDirectionDegrees,
            windStrengthPercent = state.userWindStrengthPercent,
            windDirection = state.userWindDirection,
            onWindDegreesChange = { deg ->
                component.onIntent(AddWeatherObservationStore.Intent.WindDirectionDegreesIntent(deg))
            },
            onWindStrengthChange = { pct ->
                component.onIntent(AddWeatherObservationStore.Intent.WindStrengthChangedIntent(pct))
            },
            onBack = { component.onIntent(AddWeatherObservationStore.Intent.CloseWindSetupIntent) },
            onSave = { component.onIntent(AddWeatherObservationStore.Intent.CloseWindSetupIntent) },
            modifier = modifier,
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_new_observation_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onIntent(AddWeatherObservationStore.Intent.BackIntent) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AppColors.primary,
                )
                Text(
                    text = stringResource(Res.string.label_place),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = AppColors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .heightIn(min = 48.dp),
                ) {
                    val loadError = state.loadError
                    when {
                        loadError != null -> Text(
                            text = loadError,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodySmall,
                        )

                        else -> AddObservationLocationBlock(
                            isLoadingBackground = state.isLoadingBackground,
                            coordinates = state.coordinates,
                            locationLabel = state.locationLabel,
                            color = MaterialTheme.colorScheme.onPrimary,
                            loadingText = stringResource(Res.string.header_loading),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(temperatureIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = temperatureSliderAccentColor(state.userTemperatureC),
                )
                Text(
                    text = stringResource(Res.string.label_temperature),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val displayValue = when (state.temperatureUnit) {
                        TemperatureUnit.CELSIUS -> state.userTemperatureC.toInt()
                        TemperatureUnit.FAHRENHEIT -> (state.userTemperatureC * 9 / 5 + 32).toInt()
                    }
                    val displayUnit = stringResource(
                        when (state.temperatureUnit) {
                            TemperatureUnit.CELSIUS -> Res.string.unit_celsius
                            TemperatureUnit.FAHRENHEIT -> Res.string.unit_fahrenheit
                        },
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "$displayValue$displayUnit",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = temperatureSliderAccentColor(state.userTemperatureC),
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            TemperatureUnit.entries.forEach { unit ->
                                val isSelected = state.temperatureUnit == unit
                                val label = when (unit) {
                                    TemperatureUnit.CELSIUS -> stringResource(Res.string.unit_celsius)
                                    TemperatureUnit.FAHRENHEIT -> stringResource(Res.string.unit_fahrenheit)
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
                                                component.onIntent(AddWeatherObservationStore.Intent.TemperatureUnitToggleIntent)
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
                        value = state.userTemperatureC.toFloat(),
                        onValueChange = {
                            component.onIntent(AddWeatherObservationStore.Intent.TemperatureChangedIntent(it.toDouble()))
                        },
                        valueRange = -30f..45f,
                        colors = SliderDefaults.colors(
                            thumbColor = temperatureSliderAccentColor(state.userTemperatureC),
                            activeTrackColor = temperatureSliderAccentColor(state.userTemperatureC),
                            inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                        ),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(windSectionIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AppColors.primary,
                )
                Text(
                    text = stringResource(Res.string.label_wind),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { component.onIntent(AddWeatherObservationStore.Intent.OpenWindSetupIntent) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .heightIn(min = 72.dp),
                ) {
                    if (state.userWindStrengthPercent == 0) {
                        Text(
                            text = stringResource(Res.string.wind_add_data),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.primary,
                        )
                        Text(
                            text = stringResource(Res.string.wind_still),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = windDirectionDisplayName(state.userWindDirection),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.primary,
                            )
                            WindStrengthIcons(
                                percent = state.userWindStrengthPercent,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                iconSize = 18.dp,
                            )
                        }
                        Text(
                            text = "${state.windDirectionDegrees.toInt()}°",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Text(
                        text = stringResource(Res.string.btn_open_wind_setup),
                        style = MaterialTheme.typography.labelMedium,
                        color = AppColors.primary,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(weatherSectionIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AppColors.primary,
                )
                Text(
                    text = stringResource(Res.string.label_weather),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            WeatherTypeRow(
                types = cloudinessTypes,
                selected = state.userWeatherTypes,
                onToggle = {
                    component.onIntent(AddWeatherObservationStore.Intent.WeatherTypeToggledIntent(it))
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(precipitationSectionIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AppColors.primary,
                )
                Text(
                    text = stringResource(Res.string.label_precipitation),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            WeatherTypeRow(
                types = precipitationTypes,
                selected = state.userWeatherTypes,
                onToggle = {
                    component.onIntent(AddWeatherObservationStore.Intent.WeatherTypeToggledIntent(it))
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            val saveErrorText = state.saveError?.let { key ->
                when (key) {
                    SAVE_ERROR_MISSING_BACKGROUND_KEY -> stringResource(Res.string.observation_save_missing_background)
                    else -> key
                }
            }

            Button(
                onClick = { component.onIntent(AddWeatherObservationStore.Intent.SaveIntent) },
                enabled = !state.isLoadingBackground &&
                    state.loadError == null &&
                    state.apiData != null &&
                    state.coordinates != null &&
                    state.userWeatherTypes.isNotEmpty() &&
                    !state.isSaving,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Text(
                    text = stringResource(Res.string.btn_save_observation),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }

            saveErrorText?.let { text ->
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    label: String,
    valueText: String,
    valueColor: androidx.compose.ui.graphics.Color,
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = valueText,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = valueColor,
            )
            Slider(
                value = sliderValue,
                onValueChange = onValueChange,
                valueRange = range,
            )
        }
    }
}

@Composable
private fun WeatherTypeRow(
    types: List<WeatherTypeDD>,
    selected: Set<WeatherTypeDD>,
    onToggle: (WeatherTypeDD) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        types.forEach { type ->
            val isSelected = type in selected
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) AppColors.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.medium,
                    )
                    .background(
                        color = if (isSelected) AppColors.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium,
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
