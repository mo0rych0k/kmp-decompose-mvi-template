package io.pylyp.weather.ui.skytrack.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.btn_save_observation
import io.pylyp.common.resources.header_loading
import io.pylyp.common.resources.header_now
import io.pylyp.common.resources.label_temperature
import io.pylyp.common.resources.label_weather_type
import io.pylyp.common.resources.label_wind_direction
import io.pylyp.common.resources.label_wind_strength
import io.pylyp.common.resources.screen_new_observation_title
import io.pylyp.common.resources.weather_cloudy
import io.pylyp.common.resources.weather_overcast
import io.pylyp.common.resources.weather_rain
import io.pylyp.common.resources.weather_sunny
import io.pylyp.common.resources.wind_north
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.domain.entity.WeatherTypeDD
import io.pylyp.weather.domain.entity.WindDirectionDD
import io.pylyp.weather.ui.skytrack.AddObservationLocationBlock
import io.pylyp.weather.ui.skytrack.add.store.AddWeatherObservationStore
import io.pylyp.weather.ui.skytrack.temperatureSliderAccentColor
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddWeatherObservationScreen(
    component: AddWeatherObservationComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
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
            Card(
                colors = CardDefaults.cardColors(containerColor = AppColors.primary),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(Res.string.header_now),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    val loadError = state.loadError
                    when {
                        loadError != null -> Text(
                            text = loadError,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.bodyMedium,
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

            TemperatureMetricCard(
                label = stringResource(Res.string.label_temperature),
                valueText = "${state.userTemperatureC.toInt()}°",
                valueCelsius = state.userTemperatureC,
                sliderValue = state.userTemperatureC.toFloat(),
                onValueChange = {
                    component.onIntent(AddWeatherObservationStore.Intent.TemperatureChangedIntent(it.toDouble()))
                },
                range = -30f..45f,
            )

            Text(
                text = stringResource(Res.string.label_wind_direction),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            WindDirectionRow(
                selected = state.userWindDirection,
                onSelect = {
                    component.onIntent(AddWeatherObservationStore.Intent.WindDirectionChangedIntent(it))
                },
            )

            MetricCard(
                label = stringResource(Res.string.label_wind_strength),
                valueText = "${state.userWindStrengthPercent}%",
                valueColor = AppColors.primary,
                sliderValue = state.userWindStrengthPercent.toFloat(),
                onValueChange = {
                    component.onIntent(AddWeatherObservationStore.Intent.WindStrengthChangedIntent(it.toInt()))
                },
                range = 0f..100f,
            )

            Text(
                text = stringResource(Res.string.label_weather_type),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            WeatherTypeRow(
                selected = state.userWeatherType,
                onSelect = {
                    component.onIntent(AddWeatherObservationStore.Intent.WeatherTypeChangedIntent(it))
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { component.onIntent(AddWeatherObservationStore.Intent.SaveIntent) },
                enabled = !state.isLoadingBackground && state.apiData != null && !state.isSaving,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Text(
                    text = stringResource(Res.string.btn_save_observation),
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun TemperatureMetricCard(
    label: String,
    valueText: String,
    valueCelsius: Double,
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
) {
    val accent = temperatureSliderAccentColor(valueCelsius)
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
                color = accent,
            )
            Slider(
                value = sliderValue,
                onValueChange = onValueChange,
                valueRange = range,
                colors = SliderDefaults.colors(
                    thumbColor = accent,
                    activeTrackColor = accent,
                    inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                ),
            )
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
private fun WindDirectionRow(
    selected: WindDirectionDD,
    onSelect: (WindDirectionDD) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        WindDirectionDD.entries.forEach { dir ->
            val label = when (dir) {
                WindDirectionDD.NORTH -> stringResource(Res.string.wind_north)
                else -> dir.name
            }
            Text(
                text = if (dir == selected) "▶ $label" else "  $label",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(dir) }
                    .padding(vertical = 4.dp),
                color = if (dir == selected) AppColors.primary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (dir == selected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun WeatherTypeRow(
    selected: WeatherTypeDD,
    onSelect: (WeatherTypeDD) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        WeatherTypeDD.entries.forEach { type ->
            val label = when (type) {
                WeatherTypeDD.SUNNY -> stringResource(Res.string.weather_sunny)
                WeatherTypeDD.CLOUDY -> stringResource(Res.string.weather_cloudy)
                WeatherTypeDD.OVERCAST -> stringResource(Res.string.weather_overcast)
                WeatherTypeDD.RAIN -> stringResource(Res.string.weather_rain)
            }
            Text(
                text = if (type == selected) "▶ $label" else "  $label",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(type) }
                    .padding(vertical = 4.dp),
                color = if (type == selected) AppColors.primary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (type == selected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}
