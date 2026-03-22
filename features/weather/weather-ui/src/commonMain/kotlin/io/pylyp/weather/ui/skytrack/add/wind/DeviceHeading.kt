package io.pylyp.weather.ui.skytrack.add.wind

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/** Degrees clockwise from north to the top edge of the device (0–360), or null if unavailable. */
@Composable
internal expect fun rememberDeviceHeadingDegrees(): State<Float?>
