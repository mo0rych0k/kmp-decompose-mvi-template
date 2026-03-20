package io.pylyp.weather.ui.screens.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.domain.entity.displayName
import io.pylyp.weather.ui.screens.services.store.WeatherServicesStore

@Composable
internal fun WeatherServicesScreen(
    component: WeatherServicesComponent,
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
    state: WeatherServicesStore.State,
    onIntent: (WeatherServicesStore.Intent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Weather in Kyiv",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Pick a free open-data provider (no API key):",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        state.services.forEach { service ->
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onIntent(WeatherServicesStore.Intent.OnServiceClick(service = service)) },
            ) {
                Text(text = service.displayName())
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun PreviewWeatherServicesScreen() {
    AppTheme {
        ContentScreen(
            state = WeatherServicesStore.State(),
            onIntent = {},
        )
    }
}
