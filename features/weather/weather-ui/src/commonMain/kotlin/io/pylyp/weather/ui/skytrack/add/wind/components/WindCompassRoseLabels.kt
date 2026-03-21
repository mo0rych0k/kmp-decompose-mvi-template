package io.pylyp.weather.ui.skytrack.add.wind.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppTheme

@Composable
internal fun WindCompassRoseLabelsComponent(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = "N",
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB71C1C),
        )
        Text(
            text = "E",
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "S",
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "W",
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 4.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
internal fun WindCompassRoseLabelsComponentPreview() {
    AppTheme {
        WindCompassRoseLabelsComponent()
    }
}
