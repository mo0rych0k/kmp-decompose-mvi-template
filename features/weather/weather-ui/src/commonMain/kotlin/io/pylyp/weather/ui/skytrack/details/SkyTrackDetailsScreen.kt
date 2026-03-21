package io.pylyp.weather.ui.skytrack.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.action_share
import io.pylyp.common.resources.btn_cancel
import io.pylyp.common.resources.btn_confirm
import io.pylyp.common.resources.btn_delete_record
import io.pylyp.common.resources.dialog_delete_confirm_message
import io.pylyp.common.resources.dialog_delete_confirm_title
import io.pylyp.common.resources.error_dialog_retry_button
import io.pylyp.common.resources.screen_details_title
import io.pylyp.common.uikit.components.AlertDialogComponent
import io.pylyp.weather.ui.skytrack.details.components.DetailsContentComponent
import io.pylyp.weather.ui.skytrack.details.store.SkyTrackDetailsStore
import io.pylyp.weather.ui.skytrack.model.formatObservationDateFromEpochMs
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkyTrackDetailsScreen(
    component: SkyTrackDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    val record = state.record
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialogComponent(
            title = stringResource(Res.string.dialog_delete_confirm_title),
            message = stringResource(Res.string.dialog_delete_confirm_message),
            positiveButtonText = stringResource(Res.string.btn_confirm),
            negativeButtonText = stringResource(Res.string.btn_cancel),
            onDismissRequest = { showDeleteConfirm = false },
            onPositiveButtonClicked = {
                showDeleteConfirm = false
                component.onIntent(SkyTrackDetailsStore.Intent.DeleteIntent)
            },
            onNegativeButtonClicked = { showDeleteConfirm = false },
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = record?.let { formatObservationDateFromEpochMs(it.createdAtEpochMs) }
                            ?: stringResource(Res.string.screen_details_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onIntent(SkyTrackDetailsStore.Intent.BackIntent) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if (record != null) {
                        IconButton(
                            onClick = { showDeleteConfirm = true },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(Res.string.btn_delete_record),
                            )
                        }
                        IconButton(
                            onClick = { component.onIntent(SkyTrackDetailsStore.Intent.ShareIntent) },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(Res.string.action_share),
                            )
                        }
                    }
                },
            )
        },
    ) { padding ->
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
                        onOpenMeteoClick = { component.openServiceUrl("https://open-meteo.com/") },
                    )
            }
        }
    }
}
