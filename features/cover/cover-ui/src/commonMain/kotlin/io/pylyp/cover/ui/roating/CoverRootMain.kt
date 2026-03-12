package io.pylyp.cover.ui.roating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import io.pylyp.common.core.di.IsolatedKoinContext
import io.pylyp.cover.ui.screens.cover.CoverScreen
import org.koin.compose.KoinIsolatedContext

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
                    is Cover -> CoverScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        component = child.component,
                    )
                }
            }
        }
    }
}
