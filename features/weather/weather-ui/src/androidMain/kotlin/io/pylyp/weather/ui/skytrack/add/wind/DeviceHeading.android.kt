package io.pylyp.weather.ui.skytrack.add.wind

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun rememberDeviceHeadingDegrees(): State<Float?> {
    val context = LocalContext.current
    val heading = remember { mutableStateOf<Float?>(null) }
    DisposableEffect(context) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor =
            sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) ?: sm.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        if (sensor == null) {
            heading.value = null
            return@DisposableEffect onDispose { }
        }
        val rotationMatrix = FloatArray(9)
        val orientationAngles = FloatArray(3)
        val listener =
            object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                    val azimuthRad = orientationAngles[0]
                    var deg = Math.toDegrees(azimuthRad.toDouble()).toFloat()
                    deg = (deg + 360f) % 360f
                    heading.value = deg
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
            }
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sm.unregisterListener(listener)
        }
    }
    return heading
}
