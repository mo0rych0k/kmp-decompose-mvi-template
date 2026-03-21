package io.pylyp.weather.ui.skytrack.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.details_card_title_local
import io.pylyp.common.resources.details_card_title_open_meteo
import io.pylyp.common.resources.location_unknown
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.sampleWeatherObservationRecordUi
import org.jetbrains.compose.resources.stringResource

private const val OPEN_METEO_URL = "https://open-meteo.com/"

@Composable
internal fun DetailsContentComponent(
    modifier: Modifier = Modifier,
    record: WeatherObservationRecordUi,
    onOpenMeteoClick: () -> Unit,
) {
    val unknownLabel = stringResource(Res.string.location_unknown)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        SourceDataCardComponent(
            title = stringResource(Res.string.details_card_title_local),
            record = record,
            isLocalData = true,
            unknownLocationLabel = unknownLabel,
            modifier = Modifier.fillMaxWidth(),
        )

        SourceDataCardComponent(
            title = stringResource(Res.string.details_card_title_open_meteo),
            record = record,
            isLocalData = false,
            unknownLocationLabel = unknownLabel,
            serviceUrl = OPEN_METEO_URL,
            onTitleClick = onOpenMeteoClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
internal fun DetailsContentComponentPreview() {
    AppTheme {
        DetailsContentComponent(
            record = sampleWeatherObservationRecordUi(),
            onOpenMeteoClick = {},
        )
    }
}
