package io.pylyp.weather.ui.skytrack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.label_weather
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.cloudinessTypes
import io.pylyp.weather.ui.skytrack.add.toWeatherIconRes
import io.pylyp.weather.ui.skytrack.add.weatherSectionIconRes
import io.pylyp.weather.ui.skytrack.model.WeatherTypeUi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CloudinessIconsRowComponent(
    userWeatherTypes: Set<WeatherTypeUi>,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(weatherSectionIconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = label + ":",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        val selected = cloudinessTypes.filter { it in userWeatherTypes }
        if (selected.isEmpty()) {
            Text(
                text = "—",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (type in selected) {
                    Icon(
                        painter = painterResource(type.toWeatherIconRes()),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun CloudinessIconsRowComponentPreview() {
    AppTheme {
        CloudinessIconsRowComponent(
            userWeatherTypes = setOf(WeatherTypeUi.SUNNY, WeatherTypeUi.CLOUDY),
            label = stringResource(Res.string.label_weather),
        )
    }
}
