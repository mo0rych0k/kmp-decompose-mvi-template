package io.pylyp.weather.ui.skytrack.add.wind

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import platform.CoreLocation.CLHeading
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLHeadingFilterNone
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
internal actual fun rememberDeviceHeadingDegrees(): State<Float?> {
    val heading = remember { mutableStateOf<Float?>(null) }
    DisposableEffect(Unit) {
        val manager = CLLocationManager()
        val delegate =
            object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
                    val trueH = didUpdateHeading.trueHeading
                    val deg =
                        if (trueH >= 0.0) {
                            trueH.toFloat()
                        } else {
                            didUpdateHeading.magneticHeading.toFloat()
                        }
                    dispatch_async(dispatch_get_main_queue()) {
                        heading.value = deg
                    }
                }
            }
        manager.delegate = delegate
        manager.headingFilter = kCLHeadingFilterNone
        val status = CLLocationManager.authorizationStatus()
        if (status != kCLAuthorizationStatusAuthorizedWhenInUse &&
            status != kCLAuthorizationStatusAuthorizedAlways
        ) {
            manager.requestWhenInUseAuthorization()
        }
        if (CLLocationManager.headingAvailable()) {
            manager.startUpdatingHeading()
        }
        onDispose {
            manager.stopUpdatingHeading()
            manager.delegate = null
        }
    }
    return heading
}
