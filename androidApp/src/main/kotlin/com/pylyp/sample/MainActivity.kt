package com.pylyp.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.arkivanov.decompose.defaultComponentContext
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.common.core.di.IsolatedKoinContext
import io.pylyp.sample.composeapp.App
import io.pylyp.sample.composeapp.di.createAppRootComponent
import io.pylyp.sample.composeapp.notification.WeatherNotificationAndroid
import org.koin.core.component.KoinComponent

class MainActivity : ComponentActivity(), KoinComponent {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                WeatherNotificationAndroid.ensureDailyWeatherScheduled(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            WeatherNotificationAndroid.onMainActivityLaunch(
                activity = this,
                permissionLauncher = notificationPermissionLauncher,
            )
        }
        val componentFactory: ComponentFactory by IsolatedKoinContext.koin().inject()

        val root =
            componentFactory.createAppRootComponent(
                componentContext = defaultComponentContext(),
            )

        setContent {
            App(root)
        }
    }
}
