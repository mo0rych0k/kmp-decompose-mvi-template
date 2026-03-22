package io.pylyp.weather.ui.skytrack.add.wind

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.ic_compass_arrow
import io.pylyp.weather.ui.skytrack.add.windIconRes
import io.pylyp.weather.ui.skytrack.add.windSectionIconRes
import io.pylyp.common.resources.label_wind
import io.pylyp.common.resources.label_wind_strength
import io.pylyp.common.resources.screen_wind_setup_title
import io.pylyp.common.resources.wind_azimuth_caption
import io.pylyp.common.resources.wind_setup_btn_save
import io.pylyp.common.resources.wind_setup_info_guide
import io.pylyp.common.resources.wind_setup_info_title
import io.pylyp.common.resources.wind_still
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.domain.entity.WindDirectionDD
import io.pylyp.weather.ui.skytrack.WindStrengthIcons
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WindDirectionStrengthScreen(
    windDirectionDegrees: Float,
    windStrengthPercent: Int,
    windDirection: WindDirectionDD,
    onWindDegreesChange: (Float) -> Unit,
    onWindStrengthChange: (Int) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val heading by rememberDeviceHeadingDegrees()
    var headingForGesture by remember { mutableStateOf<Float?>(null) }
    var infoSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isHeadingLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(heading) {
        headingForGesture = heading
        if (heading != null) isHeadingLoaded = true
    }
    LaunchedEffect(Unit) {
        delay(1500)
        isHeadingLoaded = true
    }
    if (infoSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { infoSheetVisible = false },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.wind_setup_info_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(Res.string.wind_setup_info_guide),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_wind_setup_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { infoSheetVisible = true },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(Res.string.wind_setup_info_title),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(windSectionIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AppColors.primary,
                )
                Text(
                    text = stringResource(Res.string.label_wind),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = windDirectionDisplayName(windDirection),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppColors.primary,
                                    )
                                    Text(
                                        text = stringResource(
                                            Res.string.wind_azimuth_caption,
                                            windDirectionDegrees.toInt(),
                                        ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
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
                                        WindCompassRoseLabels(modifier = Modifier.fillMaxSize())
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
                                        Icon(
                                            painter = painterResource(Res.drawable.ic_compass_arrow),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(120.dp)
                                                .graphicsLayer {
                                                    rotationZ = arrowRotation
                                                    transformOrigin = TransformOrigin.Center
                                                },
                                            tint = AppColors.primary,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(windIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = AppColors.primary,
                )
                Text(
                    text = stringResource(Res.string.label_wind_strength),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (windStrengthPercent == 0) {
                            Text(
                                text = stringResource(Res.string.wind_still),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = AppColors.primary,
                            )
                        } else {
                            WindStrengthIcons(
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
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(Res.string.wind_setup_btn_save),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun WindCompassRoseLabels(modifier: Modifier = Modifier) {
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
