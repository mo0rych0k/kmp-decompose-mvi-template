package io.pylyp.weather.ui.skytrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.pylyp.weather.ui.skytrack.add.windIconRes
import org.jetbrains.compose.resources.painterResource

/**
 * Maps wind strength percent (0–100) to icon count:
 * - 0: no wind
 * - 1–33: weak (1 icon)
 * - 34–66: medium (2 icons)
 * - 67–100: strong (3 icons)
 */
public fun windStrengthLevel(percent: Int): Int =
    when {
        percent <= 0 -> 0
        percent < 34 -> 1
        percent < 67 -> 2
        else -> 3
    }

@Composable
public fun WindStrengthIcons(
    percent: Int,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    iconSize: androidx.compose.ui.unit.Dp = 16.dp,
) {
    val level = windStrengthLevel(percent)
    if (level > 0) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            repeat(level) {
                Icon(
                    painter = painterResource(windIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = tint,
                )
            }
        }
    }
}
