package io.pylyp.weather.ui.skytrack.add.wind.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.wind.windDirectionDisplayName
import io.pylyp.weather.ui.skytrack.components.WindDirectionArrowIconComponent
import io.pylyp.weather.ui.skytrack.model.WindDirectionUi
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun WindDirectionCardComponent(
    windDirectionDegrees: Float,
    windStrengthPercent: Int,
    windDirection: WindDirectionUi,
    isHeadingLoaded: Boolean,
    headingForGesture: Float?,
    heading: Float?,
    onWindDegreesChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Card(
            modifier = Modifier.widthIn(max = 320.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (isHeadingLoaded) {
                        Text(
                            text = windDirectionDisplayName(windDirection),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.primary,
                        )
                    }
                }
                val compassEnabled = windStrengthPercent > 0
                val outlineColor = MaterialTheme.colorScheme.outline
                BoxWithConstraints(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(280.dp),
                ) {
                    if (!isHeadingLoaded) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(color = AppColors.primary)
                        }
                    } else {
                        val centerX = constraints.maxWidth.toFloat() / 2f
                        val centerY = constraints.maxHeight.toFloat() / 2f
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { alpha = if (compassEnabled) 1f else 0.5f }
                                .pointerInput(compassEnabled) {
                                    if (!compassEnabled) return@pointerInput
                                    detectDragGestures { change, _ ->
                                        val p = change.position
                                        val dx = p.x - centerX
                                        val dy = p.y - centerY
                                        val screenRad = atan2(dx.toDouble(), (-dy).toDouble())
                                        var screenDeg = (screenRad * 180.0 / PI).toFloat()
                                        screenDeg = (screenDeg + 360f) % 360f
                                        val h = headingForGesture ?: 0f
                                        val worldDeg = (screenDeg - h + 360f) % 360f
                                        onWindDegreesChange(worldDeg)
                                    }
                                },
                        ) {
                            val roseRotation = -(heading ?: 0f)
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        rotationZ = roseRotation
                                    },
                            ) {
                                WindCompassRoseLabelsComponent(modifier = Modifier.fillMaxSize())
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val c = Offset(size.width / 2f, size.height / 2f)
                                    drawCircle(
                                        color = outlineColor,
                                        radius = size.minDimension * 0.42f,
                                        center = c,
                                        style = Stroke(width = 3.dp.toPx()),
                                    )
                                    for (i in 0 until 8) {
                                        val ang = i * 45.0 * PI / 180.0
                                        val rOuter = size.minDimension * 0.42f
                                        val rInner = size.minDimension * 0.36f
                                        val o = Offset(
                                            (sin(ang) * rOuter).toFloat(),
                                            (-cos(ang) * rOuter).toFloat(),
                                        )
                                        val inn = Offset(
                                            (sin(ang) * rInner).toFloat(),
                                            (-cos(ang) * rInner).toFloat(),
                                        )
                                        drawLine(
                                            color = outlineColor.copy(alpha = 0.5f),
                                            start = c + inn,
                                            end = c + o,
                                            strokeWidth = 2.dp.toPx(),
                                        )
                                    }
                                }
                            }
                            val arrowRotation = windDirectionDegrees - (heading ?: 0f)
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                WindDirectionArrowIconComponent(
                                    rotationDegrees = arrowRotation,
                                    contentDescription = null,
                                    iconSize = 120.dp,
                                    tint = AppColors.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
internal fun WindDirectionCardComponentPreview() {
    AppTheme {
        WindDirectionCardComponent(
            windDirectionDegrees = 45f,
            windStrengthPercent = 50,
            windDirection = WindDirectionUi.NORTH_EAST,
            isHeadingLoaded = true,
            headingForGesture = 0f,
            heading = 0f,
            onWindDegreesChange = {},
        )
    }
}
