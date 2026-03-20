package io.pylyp.sample.composeapp.roating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.coffee.ui.roating.CoffeeRootMain
import io.pylyp.cover.ui.roating.CoverRootMain
import io.pylyp.weather.ui.roating.WeatherRootMain

@Composable
public fun AppRootMain(rootComponent: AppRootComponent) {
    val stack = rootComponent.stack.subscribeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Children(
            stack = stack.value,
            animation = stackAnimation(fade() + scale()),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .weight(1f),
        ) {
            when (val instance = it.instance) {
                is AppRootComponent.Child.Cover -> CoverRootMain(
                    component = instance.component,
                    modifier = Modifier
                        .fillMaxSize(),
                )

                is AppRootComponent.Child.Coffee -> CoffeeRootMain(
                    component = instance.component,
                    modifier = Modifier
                        .fillMaxSize(),
                )

                is AppRootComponent.Child.Weather -> WeatherRootMain(
                    component = instance.component,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }
        }
    }
}
