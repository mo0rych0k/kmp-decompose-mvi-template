package io.pylyp.weather.ui.skytrack.add.wind

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberDeviceHeadingDegrees(): State<Float?> {
    return remember { mutableStateOf<Float?>(null) }
}
