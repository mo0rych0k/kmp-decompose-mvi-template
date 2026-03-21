package io.pylyp.weather.ui.skytrack.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import io.pylyp.common.resources.alert_big_difference
import io.pylyp.common.resources.btn_delete_record
import io.pylyp.common.resources.error_dialog_retry_button
import io.pylyp.common.resources.header_api_data
import io.pylyp.common.resources.header_loading
import io.pylyp.common.resources.header_my_observations
import io.pylyp.common.resources.label_temperature
import io.pylyp.common.resources.label_weather_type
import io.pylyp.common.resources.label_wind
import io.pylyp.common.resources.location_unknown
import io.pylyp.common.resources.screen_details_title
import io.pylyp.common.resources.value_mock_api
import io.pylyp.common.resources.wind_north
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.domain.entity.WeatherObservationRecordDD
import io.pylyp.weather.domain.entity.WindDirectionDD
import io.pylyp.weather.ui.skytrack.ObservationLocationBlock
import io.pylyp.weather.ui.skytrack.add.toWeatherIconRes
import io.pylyp.weather.ui.skytrack.details.store.SkyTrackDetailsStore
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
        if (state.isLoading && record == null) {
            Text(
                text = stringResource(Res.string.header_loading),
                modifier = Modifier.padding(padding).padding(16.dp),
            )
        } else if (record == null) {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text(text = state.errorMessage ?: "")
                Button(onClick = { component.onIntent(SkyTrackDetailsStore.Intent.RetryIntent) }) {
                    Text(text = stringResource(Res.string.error_dialog_retry_button))
                }
            }
        } else {
            DetailsContent(
                modifier = Modifier.padding(padding),
                record = record,
                isHigh = record.isHighDiscrepancy,
                onDelete = { component.onIntent(SkyTrackDetailsStore.Intent.DeleteIntent) },
            )
        }
    }
}

@Composable
private fun DetailsContent(
    modifier: Modifier = Modifier,
    record: WeatherObservationRecordDD,
    isHigh: Boolean,
    onDelete: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = formatEpoch(record.createdAtEpochMs),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
                ObservationLocationBlock(
                    record = record,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    unknownLabel = stringResource(Res.string.location_unknown),
                )
            }
        }

        if (isHigh) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppColors.error.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = stringResource(Res.string.alert_big_difference),
                    color = AppColors.error,
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            Text(
                text = stringResource(Res.string.header_my_observations),
                modifier = Modifier
                    .weight(1f)
                    .background(AppColors.userDataAccent)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(Res.string.header_api_data),
                modifier = Modifier
                    .weight(1f)
                    .background(AppColors.apiDataAccent)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }

        ComparisonRow(
            label = stringResource(Res.string.label_temperature),
            user = formatTemp(record.userTemperatureC),
            api = formatTempNullable(record.apiTemperatureC),
            delta = "${abs(record.temperatureDeltaC).toInt()}°",
        )
        ComparisonRow(
            label = stringResource(Res.string.label_wind),
            user = "${windLabel(record.userWindDirection)} · ${record.userWindStrengthPercent}%",
            api = record.apiWindDescription ?: "—",
            delta = null,
        )
        ComparisonRowWithUserContent(
            label = stringResource(Res.string.label_weather_type),
            userContent = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    record.userWeatherTypes.forEach { type ->
                        Icon(
                            painter = painterResource(type.toWeatherIconRes()),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = AppColors.userDataAccent,
                        )
                    }
                }
            },
            api = record.apiDescription ?: stringResource(Res.string.value_mock_api),
            delta = null,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.error),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            Text(
                text = stringResource(Res.string.btn_delete_record),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun ComparisonRow(
    label: String,
    user: String,
    api: String,
    delta: String?,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = user,
                modifier = Modifier.weight(1f),
                color = AppColors.userDataAccent,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = api,
                modifier = Modifier.weight(1f),
                color = AppColors.apiDataAccent,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        if (delta != null) {
            Text(
                text = delta,
                color = AppColors.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ComparisonRowWithUserContent(
    label: String,
    userContent: @Composable () -> Unit,
    api: String,
    delta: String?,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                userContent()
            }
            Text(
                text = api,
                modifier = Modifier.weight(1f),
                color = AppColors.apiDataAccent,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        if (delta != null) {
            Text(
                text = delta,
                color = AppColors.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun windLabel(direction: WindDirectionDD): String {
    return if (direction == WindDirectionDD.NORTH) {
        stringResource(Res.string.wind_north)
    } else {
        direction.name
    }
}

private fun formatTemp(value: Double): String {
    val sign = if (value >= 0) "+" else ""
    return "$sign${value.toInt()}°C"
}

private fun formatTempNullable(value: Double?): String {
    val v = value ?: return "—"
    return formatTemp(v)
}

@Suppress("MagicNumber")
private fun formatEpoch(epochMs: Long): String {
    val zone = TimeZone.currentSystemDefault()
    val local = Instant.fromEpochMilliseconds(epochMs).toLocalDateTime(zone)
    return "${local.hour.twoDigits()}:${local.minute.twoDigits()}"
}

@Suppress("MagicNumber")
private fun Int.twoDigits(): String = toString().padStart(2, '0')

