package io.pylyp.weather.ui.skytrack.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.alert_big_difference
import io.pylyp.common.resources.btn_delete_record
import io.pylyp.common.resources.header_api_data
import io.pylyp.common.resources.header_my_observations
import io.pylyp.common.resources.info_comparison_wind_body
import io.pylyp.common.resources.info_comparison_wind_title
import io.pylyp.common.resources.info_details_comparison_header_body
import io.pylyp.common.resources.info_details_comparison_header_title
import io.pylyp.common.resources.info_details_discrepancy_body
import io.pylyp.common.resources.info_details_discrepancy_title
import io.pylyp.common.resources.info_details_record_header_body
import io.pylyp.common.resources.info_details_record_header_title
import io.pylyp.common.resources.label_wind
import io.pylyp.common.resources.location_unknown
import io.pylyp.common.resources.status_large_difference
import io.pylyp.common.resources.value_mock_api
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.add.cloudinessTypes
import io.pylyp.weather.ui.skytrack.add.toWeatherIconRes
import io.pylyp.weather.ui.skytrack.add.wind.windDirectionDisplayName
import io.pylyp.weather.ui.skytrack.add.windSectionIconRes
import io.pylyp.weather.ui.skytrack.components.TemperatureUserReadingRowComponent
import io.pylyp.weather.ui.skytrack.components.WindDirectionArrowIconComponent
import io.pylyp.weather.ui.skytrack.components.WindStrengthIconsComponent
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import io.pylyp.weather.ui.skytrack.model.WeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.sampleWeatherObservationRecordUi
import io.pylyp.weather.ui.skytrack.model.toCenterBearingDegrees
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DetailsContentComponent(
    modifier: Modifier = Modifier,
    record: WeatherObservationRecordUi,
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
        ObservationRecordCardComponent(
            sectionTitle = SectionTitleUi(
                title = stringResource(Res.string.info_details_record_header_title),
                infoSheetTitle = stringResource(Res.string.info_details_record_header_title),
                infoDescription = stringResource(Res.string.info_details_record_header_body),
            ),
            leadingIcon = {},
            record = record,
            unknownLocationLabel = stringResource(Res.string.location_unknown),
        )

        if (isHigh) {
            DiscrepancyAlertCardComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.status_large_difference),
                    infoSheetTitle = stringResource(Res.string.info_details_discrepancy_title),
                    infoDescription = stringResource(Res.string.info_details_discrepancy_body),
                ),
                leadingIcon = {},
                alertText = stringResource(Res.string.alert_big_difference),
            )
        }

        TemperatureUserReadingRowComponent(userTemperatureC = record.userTemperatureC)

        ComparisonColumnsHeaderComponent(
            sectionTitle = SectionTitleUi(
                title = stringResource(Res.string.info_details_comparison_header_title),
                infoSheetTitle = stringResource(Res.string.info_details_comparison_header_title),
                infoDescription = stringResource(Res.string.info_details_comparison_header_body),
            ),
            leadingIcon = {},
            myObservationsLabel = stringResource(Res.string.header_my_observations),
            apiDataLabel = stringResource(Res.string.header_api_data),
        )

        ComparisonRowWithUserContentComponent(
            label = SectionTitleUi(
                title = stringResource(Res.string.label_wind),
                infoSheetTitle = stringResource(Res.string.info_comparison_wind_title),
                infoDescription = stringResource(Res.string.info_comparison_wind_body),
            ),
            labelLeadingIcon = {
                Icon(
                    painter = painterResource(windSectionIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            userContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (record.userWindStrengthPercent == 0) {
                        Text(
                            text = "—",
                            color = AppColors.userDataAccent,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    } else {
                        WindDirectionArrowIconComponent(
                            rotationDegrees = record.userWindDirection.toCenterBearingDegrees(),
                            contentDescription = null,
                            iconSize = 22.dp,
                            tint = AppColors.userDataAccent,
                        )
                        Text(
                            text = windDirectionDisplayName(record.userWindDirection),
                            color = AppColors.userDataAccent,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        WindStrengthIconsComponent(
                            percent = record.userWindStrengthPercent,
                            tint = AppColors.userDataAccent,
                            iconSize = 18.dp,
                        )
                    }
                }
            },
            api = record.apiWindDescription ?: "—",
            delta = null,
        )
        ComparisonRowWithUserContentComponent(
            label = null,
            userContent = {
                val cloudinessSelected = cloudinessTypes.filter { it in record.userWeatherTypes }
                if (cloudinessSelected.isEmpty()) {
                    Text(
                        text = "—",
                        color = AppColors.userDataAccent,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        cloudinessSelected.forEach { type ->
                            Icon(
                                painter = painterResource(type.toWeatherIconRes()),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = AppColors.userDataAccent,
                            )
                        }
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

@Preview
@Composable
internal fun DetailsContentComponentPreview() {
    AppTheme {
        DetailsContentComponent(
            record = sampleWeatherObservationRecordUi(),
            isHigh = false,
            onDelete = {},
        )
    }
}
