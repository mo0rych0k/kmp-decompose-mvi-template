package io.pylyp.coffee.ui.screens.gallery


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.coffee.ui.screens.gallery.store.GalleryStore
import io.pylyp.common.uikit.AppTheme

@Composable
internal fun GalleryScreen(
    component: GalleryComponent,
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
    state: GalleryStore.State,
    onIntent: (GalleryStore.Intent) -> Unit,

    ) {
    val gridState = rememberLazyGridState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        LaunchedEffect(state.showedImageIndex) {
            if (state.showedImageIndex != null) {
                gridState.requestScrollToItem(index = state.showedImageIndex)
            }
        }

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Adaptive(minSize = 128.dp)
        ) {
            state.images.forEachIndexed { index, data ->
                item(key = data.id) {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(128.dp)
                            .clickable {
                                onIntent(
                                    GalleryStore.Intent.OnPressedImageIntent(index = index)
                                )
                            },
                        loading = {
                            LoadingIndicator()
                        },
                        error = {

                        },
                        model = data.url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                    )
                }
            }
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
            state = GalleryStore.State()
        )
    }
}