package io.pylyp.weather.ui.skytrack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.windIconRes
import org.jetbrains.compose.resources.painterResource

/**
 * Maps wind strength percent (0–100) to icon count:
 * - 0: no wind
 * - 1–33: weak (1 icon)
 * - 34–66: medium (2 icons)
 * - 67–100: strong (3 icons)
 */
internal fun windStrengthLevel(percent: Int): Int =
    when {
        percent <= 0 -> 0
        percent < 34 -> 1
        percent < 67 -> 2
        else -> 3
    }

@Composable
internal fun WindStrengthIconsComponent(
    percent: Int,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    iconSize: Dp = 16.dp,
) {
    val level = windStrengthLevel(percent)
    Row(
        modifier = modifier.height(iconSize),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (level > 0) {
            repeat(level) {
                Icon(
                    painter = painterResource(windIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = tint,
                )
            }
        } else {
            Box(modifier = Modifier.size(iconSize))
        }
    }
}

@Preview
@Composable
internal fun WindStrengthIconsComponentPreview() {
    AppTheme {
        WindStrengthIconsComponent(percent = 66, tint = MaterialTheme.colorScheme.onSurface)
    }
}
