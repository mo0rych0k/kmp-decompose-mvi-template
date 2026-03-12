package io.pylyp.coffee.ui.roating

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.coffee.ui.screens.details.DetailsScreen
import io.pylyp.coffee.ui.screens.gallery.GalleryScreen
import io.pylyp.common.core.di.IsolatedKoinContext
import org.koin.compose.KoinIsolatedContext

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalDecomposeApi::class)
@Composable
public fun CoffeeRootMain(
    component: CoffeeRootComponent,
    modifier: Modifier = Modifier,
) {
    KoinIsolatedContext(context = IsolatedKoinContext.koinApplication()) {
        val stack = component.stack.subscribeAsState()
        Box(modifier = modifier) {
            Children(
                stack = stack.value,
                animation = stackAnimation(
                    animator = fade() + scale(),
                ),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
            ) {
                when (val child = it.instance) {
                    is Coffee -> GalleryScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        component = child.component,
                    )

                    is Details -> DetailsScreen(
                        modifier = Modifier
                            .fillMaxSize(),
                        component = child.component,
                    )
                }
            }
        }
    }
}