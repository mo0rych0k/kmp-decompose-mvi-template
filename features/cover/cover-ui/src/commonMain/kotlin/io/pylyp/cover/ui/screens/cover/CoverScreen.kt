package io.pylyp.cover.ui.screens.cover


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.uikit.AppTheme
import io.pylyp.cover.ui.screens.cover.components.FeatureCardComponent
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Coffee App",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Choose your perfect coffee today",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
            )

            FeatureCardComponent(
                title = "Coffee Gallery",
                description = "Explore the best blends and find inspiration in every bean.",
                icon = Icons.Default.Collections,
                onClick = { onIntent(CoverStore.Intent.OnNavigateToCoffeeIntent) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureCardComponent(
                title = "SkyTrack Verification",
                description = "Compare your observations with Open-Meteo at your GPS location.",
                icon = Icons.Default.Star,
                onClick = { onIntent(CoverStore.Intent.OnNavigateToSkyTrackIntent) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureCardComponent(
                title = "Coffee Map",
                description = "Find the nearest spot to enjoy your favorite cup of coffee.",
                icon = Icons.Default.LocationOn,
                onClick = { /* TODO: Navigate to map */ },
            )
        }
    }
}

@Preview
@Composable
@Suppress("LongMethod")
internal fun CoverScreenContentPreview() {
    AppTheme {
        ContentScreen(
            onIntent = {},
            state = CoverStore.State()
        )
    }
}
