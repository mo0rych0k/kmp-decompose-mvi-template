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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.temperatureIconRes
import io.pylyp.weather.ui.skytrack.model.formatTemperatureDegreesDisplay
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun TemperatureUserReadingRowComponent(
    userTemperatureC: Double,
    label: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(temperatureIconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (label != null) {
            Text(
                text = label + ":",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = formatTemperatureDegreesDisplay(userTemperatureC),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Black,
            color = AppColors.userDataAccent,
        )
    }
}

@Preview
@Composable
internal fun TemperatureUserReadingRowComponentPreview() {
    AppTheme {
        TemperatureUserReadingRowComponent(userTemperatureC = 12.0)
    }
}
