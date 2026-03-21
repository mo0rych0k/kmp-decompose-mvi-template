package io.pylyp.weather.ui.skytrack.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.details_card_title_local
import io.pylyp.common.resources.details_card_title_open_meteo
import io.pylyp.common.resources.label_precipitation
import io.pylyp.common.resources.label_temperature
import io.pylyp.common.resources.label_weather
import io.pylyp.common.resources.label_wind
import io.pylyp.common.resources.location_unknown
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.precipitationSectionIconRes
import io.pylyp.weather.ui.skytrack.add.precipitationTypes
import io.pylyp.weather.ui.skytrack.add.temperatureIconRes
import io.pylyp.weather.ui.skytrack.add.toWeatherIconRes
import io.pylyp.weather.ui.skytrack.add.windSectionIconRes
import io.pylyp.weather.ui.skytrack.components.CloudinessIconsRowComponent
import io.pylyp.weather.ui.skytrack.components.ObservationLocationBlockComponent
import io.pylyp.weather.ui.skytrack.components.TemperatureUserReadingRowComponent
import io.pylyp.weather.ui.skytrack.components.WindDirectionArrowIconComponent
import io.pylyp.weather.ui.skytrack.components.WindStrengthIconsComponent
import io.pylyp.weather.ui.skytrack.details.ApiDataDisplayUi
import io.pylyp.weather.ui.skytrack.details.mapApiDataToDisplay
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.WeatherTypeUi
import io.pylyp.weather.ui.skytrack.model.formatTemperatureDegreesDisplay
import io.pylyp.weather.ui.skytrack.model.sampleWeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.toCenterBearingDegrees
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/** Styled like [ObservationListItemComponent]: same card shape, elevation, padding. */
@Composable
internal fun SourceDataCardComponent(
    title: String,
    record: WeatherObservationRecordUi,
    isLocalData: Boolean,
    unknownLocationLabel: String,
    serviceUrl: String? = null,
    onTitleClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val isTitleClickable = serviceUrl != null && onTitleClick != null
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (isTitleClickable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = if (isTitleClickable) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTitleClick,
                    )
                } else {
                    Modifier
                },
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                    unknownLabel = unknownLocationLabel,
                    showCoordinates = false,
                    placeLabelStyle = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
            }
            if (isLocalData) {
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
                LocalWindRow(record = record, timeText = formatTime(record.createdAtEpochMs))
            } else {
                val apiDisplay = mapApiDataToDisplay(
                    apiDescription = record.apiDescription,
                    apiWindDescription = record.apiWindDescription,
                )
                ApiTemperatureRow(temperatureC = record.apiTemperatureC)
                CloudinessIconsRowComponent(
                    userWeatherTypes = apiDisplay.cloudinessTypes,
                    label = stringResource(Res.string.label_weather),
                )
                PrecipitationIconsRow(
                    userWeatherTypes = apiDisplay.precipitationTypes,
                    label = stringResource(Res.string.label_precipitation),
                )
                ApiWindRowLikeLocal(
                    apiDisplay = apiDisplay,
                    timeText = formatTime(record.createdAtEpochMs),
                )
            }
        }
    }
}

@Composable
private fun LocalWindRow(record: WeatherObservationRecordUi, timeText: String) {
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
            text = timeText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ApiTemperatureRow(temperatureC: Double?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(temperatureIconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(Res.string.label_temperature) + ":",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = temperatureC?.let { formatTemperatureDegreesDisplay(it) } ?: "—",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Black,
            color = AppColors.apiDataAccent,
        )
    }
}

@Composable
private fun ApiWindRowLikeLocal(apiDisplay: ApiDataDisplayUi, timeText: String) {
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
            if (apiDisplay.windDirection == null && apiDisplay.windStrengthPercent == 0) {
                Text(
                    text = "—",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                apiDisplay.windDirection?.let { dir ->
                    WindDirectionArrowIconComponent(
                        rotationDegrees = dir.toCenterBearingDegrees(),
                        contentDescription = null,
                        iconSize = 18.dp,
                        tint = AppColors.apiDataAccent,
                    )
                }
                WindStrengthIconsComponent(
                    percent = apiDisplay.windStrengthPercent,
                    tint = AppColors.apiDataAccent,
                    iconSize = 14.dp,
                )
                apiDisplay.windSpeedKmh?.let { kmh ->
                    Text(
                        text = "$kmh km/h",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppColors.apiDataAccent,
                    )
                }
            }
        }
        Text(
            text = timeText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
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
internal fun SourceDataCardComponentLocalPreview() {
    AppTheme {
        SourceDataCardComponent(
            title = stringResource(Res.string.details_card_title_local),
            record = sampleWeatherObservationRecordUi(),
            isLocalData = true,
            unknownLocationLabel = stringResource(Res.string.location_unknown),
        )
    }
}

@Preview
@Composable
internal fun SourceDataCardComponentApiPreview() {
    AppTheme {
        SourceDataCardComponent(
            title = stringResource(Res.string.details_card_title_open_meteo),
            record = sampleWeatherObservationRecordUi(),
            isLocalData = false,
            unknownLocationLabel = stringResource(Res.string.location_unknown),
        )
    }
}
