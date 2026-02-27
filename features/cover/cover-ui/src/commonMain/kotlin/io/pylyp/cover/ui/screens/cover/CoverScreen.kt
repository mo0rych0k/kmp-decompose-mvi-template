package io.pylyp.cover.ui.screens.cover


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.uikit.AppTheme
import io.pylyp.cover.ui.screens.cover.store.CoverStore

@Composable
internal fun CoverScreen(
    component: CoverComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()

    ContentScreen(
        modifier = modifier,
        state = state,
    ) { intent ->
        component.onIntent(intent = intent)
    }
}

@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    state: CoverStore.State,
    onIntent: (CoverStore.Intent) -> Unit,
) {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text("Compose: $greeting")
            }
        }
    }

}

internal class Greeting {
    private val platform = "!111"

    fun greet(): String {
        return "Hello, ${platform}!"
    }
}

@Preview
@Composable
@Suppress("LongMethod")
internal fun PreviewChatScreen() {
    AppTheme {
        ContentScreen(
            onIntent = {},
            state = CoverStore.State()
        )
    }
}