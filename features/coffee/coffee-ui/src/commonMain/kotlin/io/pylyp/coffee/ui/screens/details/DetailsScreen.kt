package io.pylyp.coffee.ui.screens.details


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.coffee.ui.screens.details.store.DetailsStore
import io.pylyp.common.uikit.AppTheme

@Composable
internal fun DetailsScreen(
    component: DetailsComponent,
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    state: DetailsStore.State,
    onIntent: (DetailsStore.Intent) -> Unit,
) {

    Column(
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.images.isEmpty()) return

        val pagerState = rememberPagerState(
            initialPage = state.showedImageIndex ?: 0,
            pageCount = { state.images.size },
        )

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }
                .collect { page ->
                    onIntent(DetailsStore.Intent.OnPageChanged(page))
                }
        }

        HorizontalPager(
            state = pagerState,
        ) { page ->
            SubcomposeAsyncImage(
                modifier = Modifier
                    .aspectRatio(1F)
                    .fillMaxSize(),
                loading = {
                    LoadingIndicator()
                },
                error = {

                },
                model = state.images[page].url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
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
            state = DetailsStore.State()
        )
    }
}