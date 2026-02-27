package io.pylyp.cover.ui.roating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import io.pylyp.cover.ui.di.createCoverRootComponent
import io.pylyp.cover.ui.screens.cover.CoverScreen
import org.koin.compose.KoinIsolatedContext
import org.koin.compose.koinInject

@Composable
public fun CoverRootMain(
    component: CoverRootComponent,
    modifier: Modifier = Modifier,
) {
    KoinIsolatedContext(context = IsolatedKoinContext.koinApplication()) {
        val stack = component.stack.subscribeAsState()
        Box(modifier = modifier) {
            Children(
                stack = stack.value,
                animation = stackAnimation(fade() + scale()),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
            ) {
                when (val child = it.instance) {
                    is CoverRootComponent.Child.Cover ->
                        CoverScreen(
                            modifier = Modifier
                                .fillMaxSize(),
                            component = child.component,
                        )
                }
            }
        }
    }
}

@Composable
public fun rememberCoverRootComponent(): CoverRootComponent {
    val componentFactory = koinInject<ComponentFactory>()
    return remember {
        componentFactory.createCoverRootComponent(
            componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry()),
        )
    }
}