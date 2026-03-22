package io.pylyp.weather.ui.skytrack.add

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.a11y_show_screen_field_info
import io.pylyp.common.resources.btn_open_wind_setup
import io.pylyp.common.resources.btn_save_observation
import io.pylyp.common.resources.header_loading
import io.pylyp.common.resources.info_section_cloudiness_body
import io.pylyp.common.resources.info_section_cloudiness_title
import io.pylyp.common.resources.info_section_place_body
import io.pylyp.common.resources.info_section_place_title
import io.pylyp.common.resources.info_section_precipitation_body
import io.pylyp.common.resources.info_section_precipitation_title
import io.pylyp.common.resources.info_section_temperature_body
import io.pylyp.common.resources.info_section_temperature_title
import io.pylyp.common.resources.info_section_wind_summary_body
import io.pylyp.common.resources.info_section_wind_summary_title
import io.pylyp.common.resources.label_place
import io.pylyp.common.resources.label_precipitation
import io.pylyp.common.resources.label_temperature
import io.pylyp.common.resources.label_weather
import io.pylyp.common.resources.label_wind
import io.pylyp.common.resources.label_wind_direction
import io.pylyp.common.resources.label_wind_strength
import io.pylyp.common.resources.observation_save_missing_background
import io.pylyp.common.resources.screen_field_info_title
import io.pylyp.common.resources.screen_new_observation_title
import io.pylyp.common.resources.unit_celsius
import io.pylyp.common.resources.unit_fahrenheit
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.ui.skytrack.add.components.IconPickerComponent
import io.pylyp.weather.ui.skytrack.add.components.ObservationPlaceCardComponent
import io.pylyp.weather.ui.skytrack.add.components.TemperatureCardComponent
import io.pylyp.weather.ui.skytrack.add.components.WindSummaryCardComponent
import io.pylyp.weather.ui.skytrack.add.store.AddWeatherObservationStore
import io.pylyp.weather.ui.skytrack.add.store.SAVE_ERROR_MISSING_BACKGROUND_KEY
import io.pylyp.weather.ui.skytrack.add.wind.WindDirectionStrengthScreen
import io.pylyp.weather.ui.skytrack.components.ScreenInfoModalBottomSheetComponent
import io.pylyp.weather.ui.skytrack.model.InfoSectionUi
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import io.pylyp.weather.ui.skytrack.temperatureSliderAccentColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddWeatherObservationScreen(
    component: AddWeatherObservationComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    var infoSheetVisible by remember { mutableStateOf(false) }
    val observationInfoSections = listOf(
        InfoSectionUi(
            title = stringResource(Res.string.info_section_place_title),
            body = stringResource(Res.string.info_section_place_body),
        ),
        InfoSectionUi(
            title = stringResource(Res.string.info_section_temperature_title),
            body = stringResource(Res.string.info_section_temperature_body),
        ),
        InfoSectionUi(
            title = stringResource(Res.string.info_section_wind_summary_title),
            body = stringResource(Res.string.info_section_wind_summary_body),
        ),
        InfoSectionUi(
            title = stringResource(Res.string.info_section_cloudiness_title),
            body = stringResource(Res.string.info_section_cloudiness_body),
        ),
        InfoSectionUi(
            title = stringResource(Res.string.info_section_precipitation_title),
            body = stringResource(Res.string.info_section_precipitation_body),
        ),
    )
    if (state.isWindSetupVisible) {
        WindDirectionStrengthScreen(
            windDirectionDegrees = state.windDirectionDegrees,
            windStrengthPercent = state.userWindStrengthPercent,
            windDirection = state.userWindDirection,
            onWindDegreesChange = { deg ->
                component.onIntent(AddWeatherObservationStore.Intent.WindDirectionDegreesIntent(deg))
            },
            onWindStrengthChange = { pct ->
                component.onIntent(AddWeatherObservationStore.Intent.WindStrengthChangedIntent(pct))
            },
            onBack = { component.onIntent(AddWeatherObservationStore.Intent.CloseWindSetupIntent) },
            onSave = { component.onIntent(AddWeatherObservationStore.Intent.CloseWindSetupIntent) },
            modifier = modifier,
        )
        return
    }

    ScreenInfoModalBottomSheetComponent(
        visible = infoSheetVisible,
        onDismissRequest = { infoSheetVisible = false },
        sheetTitle = stringResource(Res.string.screen_field_info_title),
        sections = observationInfoSections,
    )

    val saveErrorText = state.saveError?.let { key ->
        when (key) {
            SAVE_ERROR_MISSING_BACKGROUND_KEY -> stringResource(Res.string.observation_save_missing_background)
            else -> key
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_new_observation_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onIntent(AddWeatherObservationStore.Intent.BackIntent) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { infoSheetVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(Res.string.a11y_show_screen_field_info),
                        )
                    }
                },
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = { component.onIntent(AddWeatherObservationStore.Intent.SaveIntent) },
                    enabled = !state.isLoadingBackground &&
                        state.loadError == null &&
                        state.apiData != null &&
                        state.coordinates != null &&
                        state.userWeatherTypes.isNotEmpty() &&
                        !state.isSaving,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text(
                        text = stringResource(Res.string.btn_save_observation),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
                saveErrorText?.let { text ->
                    Text(
                        text = text,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ObservationPlaceCardComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.label_place),
                    infoSheetTitle = "",
                    infoDescription = "",
                ),
                showInfoButton = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = AppColors.primary,
                    )
                },
                loadError = state.loadError,
                isLoadingBackground = state.isLoadingBackground,
                coordinates = state.coordinates,
                locationLabel = state.locationLabel,
                loadingText = stringResource(Res.string.header_loading),
            )

            TemperatureCardComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.label_temperature),
                    infoSheetTitle = "",
                    infoDescription = "",
                ),
                showInfoButton = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(temperatureIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = temperatureSliderAccentColor(
                            state.userTemperatureC,
                            isLightTheme = !isSystemInDarkTheme(),
                        ),
                    )
                },
                temperatureUnit = state.temperatureUnit,
                userTemperatureC = state.userTemperatureC,
                unitCelsiusLabel = stringResource(Res.string.unit_celsius),
                unitFahrenheitLabel = stringResource(Res.string.unit_fahrenheit),
                onTemperatureChange = {
                    component.onIntent(AddWeatherObservationStore.Intent.TemperatureChangedIntent(it))
                },
                onUnitToggle = {
                    component.onIntent(AddWeatherObservationStore.Intent.TemperatureUnitToggleIntent)
                },
            )

            WindSummaryCardComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.label_wind),
                    infoSheetTitle = "",
                    infoDescription = "",
                ),
                showInfoButton = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(windSectionIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = AppColors.primary,
                    )
                },
                windDirectionLabel = stringResource(Res.string.label_wind_direction),
                windStrengthLabel = stringResource(Res.string.label_wind_strength),
                userWindStrengthPercent = state.userWindStrengthPercent,
                userWindDirection = state.userWindDirection,
                windDirectionDegrees = state.windDirectionDegrees,
                openWindSetupLabel = stringResource(Res.string.btn_open_wind_setup),
                onOpenWindSetup = {
                    component.onIntent(AddWeatherObservationStore.Intent.OpenWindSetupIntent)
                },
            )

            IconPickerComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.label_weather),
                    infoSheetTitle = "",
                    infoDescription = "",
                ),
                showInfoButton = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(weatherSectionIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = AppColors.primary,
                    )
                },
                types = cloudinessTypes,
                selected = state.userWeatherTypes,
                onToggle = {
                    component.onIntent(AddWeatherObservationStore.Intent.WeatherTypeToggledIntent(it))
                },
            )

            IconPickerComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.label_precipitation),
                    infoSheetTitle = "",
                    infoDescription = "",
                ),
                showInfoButton = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(precipitationSectionIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = AppColors.primary,
                    )
                },
                types = precipitationTypes,
                selected = state.userWeatherTypes,
                onToggle = {
                    component.onIntent(AddWeatherObservationStore.Intent.WeatherTypeToggledIntent(it))
                },
            )
        }
    }
}
