package io.pylyp.weather.ui.skytrack.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.ic_compass_arrow
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import org.jetbrains.compose.resources.painterResource

/**
 * Wind direction arrow (`ic_compass_arrow`) rotated so **0° = North**, **90° = East**,
 * **180° = South**, **270° = West** (meteorological bearing, clockwise from north).
 *
 * @param rotationDegrees Clockwise rotation in degrees (positive = clockwise).
 */
@Composable
internal fun WindDirectionArrowIconComponent(
    rotationDegrees: Float,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    tint: Color = AppColors.primary,
) {
    Icon(
        painter = painterResource(Res.drawable.ic_compass_arrow),
        contentDescription = contentDescription,
        modifier = modifier
            .size(iconSize)
            .graphicsLayer {
                rotationZ = rotationDegrees
                transformOrigin = TransformOrigin.Center
            },
        tint = tint,
    )
}

@Preview
@Composable
internal fun WindDirectionArrowIconComponentPreview() {
    AppTheme {
        WindDirectionArrowIconComponent(
            rotationDegrees = 135f,
            contentDescription = null,
            iconSize = 48.dp,
        )
    }
}
