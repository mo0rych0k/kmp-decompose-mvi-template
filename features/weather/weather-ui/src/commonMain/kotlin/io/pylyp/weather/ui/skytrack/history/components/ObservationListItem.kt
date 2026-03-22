package io.pylyp.weather.ui.skytrack.history.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.action_share
import io.pylyp.common.resources.btn_delete_record
import io.pylyp.common.resources.label_precipitation
import io.pylyp.common.resources.label_temperature
import io.pylyp.common.resources.label_weather
import io.pylyp.common.resources.label_wind
import io.pylyp.common.resources.location_unknown
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.precipitationSectionIconRes
import io.pylyp.weather.ui.skytrack.add.precipitationTypes
import io.pylyp.weather.ui.skytrack.add.toWeatherIconRes
import io.pylyp.weather.ui.skytrack.add.windSectionIconRes
import io.pylyp.weather.ui.skytrack.components.CloudinessIconsRowComponent
import io.pylyp.weather.ui.skytrack.components.ObservationLocationBlockComponent
import io.pylyp.weather.ui.skytrack.components.TemperatureUserReadingRowComponent
import io.pylyp.weather.ui.skytrack.components.WindDirectionArrowIconComponent
import io.pylyp.weather.ui.skytrack.components.WindStrengthIconsComponent
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.WeatherTypeUi
import io.pylyp.weather.ui.skytrack.model.sampleWeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.toCenterBearingDegrees
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun ObservationListItemComponent(
    record: WeatherObservationRecordUi,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    ObservationLocationBlockComponent(
                        location = record.location,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        unknownLabel = stringResource(Res.string.location_unknown),
                        showCoordinates = false,
                        placeLabelStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                }
                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.action_share)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onShare()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.btn_delete_record)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            },
                        )
                    }
                }
            }
            TemperatureUserReadingRowComponent(
                userTemperatureC = record.userTemperatureC,
                label = stringResource(Res.string.label_temperature),
            )
            CloudinessIconsRowComponent(
                userWeatherTypes = record.userWeatherTypes,
                label = stringResource(Res.string.label_weather),
            )
            PrecipitationIconsRow(
                userWeatherTypes = record.userWeatherTypes,
                label = stringResource(Res.string.label_precipitation),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(
                        painter = painterResource(windSectionIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(Res.string.label_wind) + ":",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (record.userWindStrengthPercent == 0) {
                        Text(
                            text = "—",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        WindDirectionArrowIconComponent(
                            rotationDegrees = record.userWindDirection.toCenterBearingDegrees(),
                            contentDescription = null,
                            iconSize = 18.dp,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        WindStrengthIconsComponent(
                            percent = record.userWindStrengthPercent,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            iconSize = 14.dp,
                        )
                    }
                }
                Text(
                    text = formatTime(record.createdAtEpochMs),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun PrecipitationIconsRow(
    userWeatherTypes: Set<WeatherTypeUi>,
    label: String,
) {
    val selected = precipitationTypes.filter { it in userWeatherTypes }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(precipitationSectionIconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = label + ":",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (selected.isEmpty()) {
            Text(
                text = "—",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (type in selected) {
                    Icon(
                        painter = painterResource(type.toWeatherIconRes()),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
    return "${recordLocal.hour.twoDigits()}:${recordLocal.minute.twoDigits()}"
}

@Suppress("MagicNumber")
private fun Int.twoDigits(): String = toString().padStart(2, '0')

@Preview
@Composable
internal fun ObservationListItemComponentPreview() {
    AppTheme {
        ObservationListItemComponent(
            record = sampleWeatherObservationRecordUi(),
            onClick = {},
            onDelete = {},
            onShare = {},
        )
    }
}
