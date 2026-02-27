package io.pylyp.sample.composeapp.roating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.pylyp.core.di.ComponentFactory
import io.pylyp.core.di.IsolatedKoinContext
import io.pylyp.cover.ui.roating.CoverRootMain
import io.pylyp.sample.composeapp.di.createAppRootComponent
import org.koin.compose.KoinIsolatedContext
import org.koin.compose.koinInject

@Composable
public fun AppRootMain() {
    KoinIsolatedContext(context = IsolatedKoinContext.koinApplication()) {
        val root = rememberAppRootComponent()
        val stack = root.stack.subscribeAsState()

        Column(modifier = Modifier.fillMaxSize()) {
            Children(
                stack = stack.value,
                animation = stackAnimation(fade() + scale()),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .weight(1f),
            ) {
                when (val instance = it.instance) {
                    is AppRootComponent.Child.Cover -> {
                        CoverRootMain(
                            component = instance.component,
                            modifier = Modifier
                                .fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberAppRootComponent(): AppRootComponent {
    val componentFactory = koinInject<ComponentFactory>()
    return remember {
        componentFactory.createAppRootComponent(
            componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry()),
        )
    }
}