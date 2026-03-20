package io.pylyp.weather.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.domain.entity.WeatherServiceType
import io.pylyp.weather.ui.screens.details.store.WeatherDetailsStore

@Composable
internal fun WeatherDetailsScreen(
    component: WeatherDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()

    ContentScreen(
        modifier = modifier,
        state = state,
        onIntent = { component.onIntent(it) },
    )
}

@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    state: WeatherDetailsStore.State,
    onIntent: (WeatherDetailsStore.Intent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = state.locationName,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = state.serviceDisplayName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            state.errorMessage != null -> {
                Text(
                    text = state.errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onIntent(WeatherDetailsStore.Intent.RetryIntent) },
                ) {
                    Text(text = "Retry")
                }
            }

            else -> {
                WeatherRow(label = "Temperature", value = state.temperatureText)
                WeatherRow(label = "Condition", value = state.conditionText)
                WeatherRow(label = "Wind", value = state.windText)
                WeatherRow(label = "Humidity", value = state.humidityText)
                WeatherRow(label = "Observed at", value = state.observedAtText)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun WeatherRow(
    label: String,
    value: String?,
) {
    if (value.isNullOrBlank()) return
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun PreviewWeatherDetailsScreen() {
    AppTheme {
        ContentScreen(
            state = WeatherDetailsStore.State(
                serviceType = WeatherServiceType.OPEN_METEO,
                serviceDisplayName = "Open-Meteo",
                isLoading = false,
                temperatureText = "7.3 °C",
                conditionText = "Mainly clear / partly cloudy / overcast",
                windText = "5 km/h",
                humidityText = null,
                observedAtText = "2026-03-19T17:30",
            ),
            onIntent = {},
        )
    }
}
