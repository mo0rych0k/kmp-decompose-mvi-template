package io.pylyp.weather.ui.skytrack.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import io.pylyp.common.resources.label_difference
import io.pylyp.common.resources.label_temperature
import io.pylyp.common.resources.label_vs
import io.pylyp.common.resources.label_wind_strength
import io.pylyp.common.resources.location_unknown
import io.pylyp.common.resources.observation_empty_description
import io.pylyp.common.resources.observation_empty_title
import io.pylyp.common.resources.observation_history_title
import io.pylyp.common.resources.status_large_difference
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.ui.skytrack.ObservationLocationBlock
import io.pylyp.weather.ui.skytrack.history.store.SkyTrackHistoryStore
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkyTrackHistoryScreen(
    component: SkyTrackHistoryComponent,
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
                        text = stringResource(Res.string.observation_history_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onIntent(SkyTrackHistoryStore.Intent.BackIntent) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { component.onIntent(SkyTrackHistoryStore.Intent.OpenAddIntent) },
                containerColor = AppColors.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
    ) { padding ->
        if (state.records.isEmpty() && !state.isLoading) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.observation_empty_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.observation_empty_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.records, key = { it.id }) { record ->
                    ObservationListItem(
                        record = record,
                        onClick = {
                            component.onIntent(SkyTrackHistoryStore.Intent.OpenDetailsIntent(recordId = record.id))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ObservationListItem(
    record: WeatherObservationRecordDD,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatTime(record.createdAtEpochMs),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                if (record.isHighDiscrepancy) {
                    Text(
                        text = stringResource(Res.string.status_large_difference),
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColors.error,
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
            ObservationLocationBlock(
                record = record,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                unknownLabel = stringResource(Res.string.location_unknown),
            )
            Text(
                text = stringResource(Res.string.label_wind_strength) + ": ${record.userWindStrengthPercent}%",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.label_temperature),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = formatTemp(record.userTemperatureC),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = AppColors.userDataAccent,
                        )
                        Text(
                            text = " " + stringResource(Res.string.label_vs) + " ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = formatTemp(record.apiTemperatureC),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = AppColors.apiDataAccent,
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(Res.string.label_difference),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "${abs(record.temperatureDeltaC).toInt()}°",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = AppColors.error,
                    )
                }
            }
        }
    }
}

@Suppress("MagicNumber")
private fun formatTime(epochMs: Long): String {
    val zone = TimeZone.currentSystemDefault()
    val stdInstant = Instant.fromEpochMilliseconds(epochMs)
    val recordLocal = stdInstant.toLocalDateTime(zone)
    val timePart = "${recordLocal.hour.twoDigits()}:${recordLocal.minute.twoDigits()}"
    return "${recordLocal.date} $timePart"
}

@Suppress("MagicNumber")
private fun Int.twoDigits(): String = toString().padStart(2, '0')

private fun formatTemp(value: Double?): String {
    val v = value ?: return "—"
    val sign = if (v >= 0) "+" else ""
    return "$sign${v.toInt()}°"
}
