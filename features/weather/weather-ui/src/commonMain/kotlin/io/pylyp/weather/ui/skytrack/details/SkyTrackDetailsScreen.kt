package io.pylyp.weather.ui.skytrack.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.error_dialog_retry_button
import io.pylyp.common.resources.screen_details_title
import io.pylyp.weather.ui.skytrack.details.components.DetailsContentComponent
import io.pylyp.weather.ui.skytrack.details.store.SkyTrackDetailsStore
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkyTrackDetailsScreen(
    component: SkyTrackDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_details_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onIntent(SkyTrackDetailsStore.Intent.BackIntent) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { padding ->
        val record = state.record
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .heightIn(min = 320.dp),
        ) {
            when {
                state.isLoading && record == null ->
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )

                record == null ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        Text(text = state.errorMessage ?: "")
                        Button(onClick = { component.onIntent(SkyTrackDetailsStore.Intent.RetryIntent) }) {
                            Text(text = stringResource(Res.string.error_dialog_retry_button))
                        }
                    }

                else ->
                    DetailsContentComponent(
                        modifier = Modifier.fillMaxSize(),
                        record = record,
                        isHigh = record.isHighDiscrepancy,
                        onDelete = { component.onIntent(SkyTrackDetailsStore.Intent.DeleteIntent) },
                    )
            }
        }
    }
}
