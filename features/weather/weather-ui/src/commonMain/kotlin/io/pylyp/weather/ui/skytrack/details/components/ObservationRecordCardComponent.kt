package io.pylyp.weather.ui.skytrack.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.location_unknown
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.components.ObservationLocationBlockComponent
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.sampleWeatherObservationRecordUi
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
internal fun ObservationRecordCardComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    record: WeatherObservationRecordUi,
    unknownLocationLabel: String,
    modifier: Modifier = Modifier,
) {
    TitleComponent(
        sectionTitle = sectionTitle,
        leadingIcon = leadingIcon,
        modifier = Modifier.fillMaxWidth(),
    )
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = formatEpoch(record.createdAtEpochMs),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            ObservationLocationBlockComponent(
                location = record.location,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                unknownLabel = unknownLocationLabel,
            )
        }
    }
}

@Suppress("MagicNumber")
private fun formatEpoch(epochMs: Long): String {
    val zone = TimeZone.currentSystemDefault()
    val local = Instant.fromEpochMilliseconds(epochMs).toLocalDateTime(zone)
    return "${local.hour.twoDigits()}:${local.minute.twoDigits()}"
}

@Suppress("MagicNumber")
private fun Int.twoDigits(): String = toString().padStart(2, '0')

@Preview
@Composable
internal fun ObservationRecordCardComponentPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ObservationRecordCardComponent(
                sectionTitle = SectionTitleUi(
                    title = "Recorded observation",
                    infoSheetTitle = "Recorded observation",
                    infoDescription = "Time and place.",
                ),
                leadingIcon = {},
                record = sampleWeatherObservationRecordUi(),
                unknownLocationLabel = stringResource(Res.string.location_unknown),
            )
        }
    }
}
