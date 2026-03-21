package io.pylyp.weather.ui.skytrack.add.wind.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import io.pylyp.weather.ui.skytrack.components.WindStrengthIconsComponent

@Composable
internal fun WindStrengthCardComponent(
    windStrengthPercent: Int,
    onWindStrengthChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (windStrengthPercent == 0) {
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = AppColors.primary,
                    )
                } else {
                    WindStrengthIconsComponent(
                        percent = windStrengthPercent,
                        tint = AppColors.primary,
                        iconSize = 24.dp,
                    )
                }
            }
            Slider(
                modifier = Modifier.padding(horizontal = 8.dp),
                value = windStrengthPercent.toFloat().coerceIn(0f, 100f),
                onValueChange = { v -> onWindStrengthChange(v.toInt().coerceIn(0, 100)) },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = AppColors.primary,
                    activeTrackColor = AppColors.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f),
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
internal fun WindStrengthCardComponentPreview() {
    AppTheme {
        WindStrengthCardComponent(
            windStrengthPercent = 40,
            onWindStrengthChange = {},
        )
    }
}
