package io.pylyp.cover.ui.screens.cover


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            FeatureCard(
                title = "Coffee Gallery",
                description = "Explore the best blends and find inspiration in every bean.",
                icon = Icons.Default.Collections,
                onClick = { onIntent(CoverStore.Intent.OnNavigateToCoffeeIntent) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureCard(
                title = "Weather Kyiv",
                description = "Current weather from free open APIs.",
                icon = Icons.Default.Cloud,
                onClick = { onIntent(CoverStore.Intent.OnNavigateToWeatherIntent) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureCard(
                title = "SkyTrack Verification",
                description = "Compare your observations with OpenWeather at your GPS location.",
                icon = Icons.Default.Star,
                onClick = { onIntent(CoverStore.Intent.OnNavigateToSkyTrackIntent) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureCard(
                title = "Coffee Map",
                description = "Find the nearest spot to enjoy your favorite cup of coffee.",
                icon = Icons.Default.LocationOn,
                onClick = { /* TODO: Navigate to map */ },
            )
        }
    }
}

@Composable
private fun FeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
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
