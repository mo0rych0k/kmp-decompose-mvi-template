package io.pylyp.weather.ui.skytrack.add.wind

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.a11y_show_screen_field_info
import io.pylyp.common.resources.info_wind_setup_strength_section_body
import io.pylyp.common.resources.info_wind_setup_strength_section_title
import io.pylyp.common.resources.info_wind_setup_wind_section_body
import io.pylyp.common.resources.info_wind_setup_wind_section_title
import io.pylyp.common.resources.label_wind_direction
import io.pylyp.common.resources.label_wind_strength
import io.pylyp.common.resources.screen_field_info_title
import io.pylyp.common.resources.screen_wind_setup_title
import io.pylyp.common.resources.wind_setup_btn_save
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.ui.skytrack.add.wind.components.WindDirectionCardComponent
import io.pylyp.weather.ui.skytrack.add.wind.components.WindStrengthCardComponent
import io.pylyp.weather.ui.skytrack.add.windIconRes
import io.pylyp.weather.ui.skytrack.add.windSectionIconRes
import io.pylyp.weather.ui.skytrack.components.ScreenInfoModalBottomSheetComponent
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.model.InfoSectionUi
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import io.pylyp.weather.ui.skytrack.model.WindDirectionUi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WindDirectionStrengthScreen(
    windDirectionDegrees: Float,
    windStrengthPercent: Int,
    windDirection: WindDirectionUi,
    onWindDegreesChange: (Float) -> Unit,
    onWindStrengthChange: (Int) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val heading by rememberDeviceHeadingDegrees()
    var headingForGesture by remember { mutableStateOf<Float?>(null) }
    var isHeadingLoaded by remember { mutableStateOf(false) }
    LaunchedEffect(heading) {
        headingForGesture = heading
        if (heading != null) isHeadingLoaded = true
    }
    LaunchedEffect(Unit) {
        delay(1500)
        isHeadingLoaded = true
    }
    var infoSheetVisible by remember { mutableStateOf(false) }
    val windInfoSections = listOf(
        InfoSectionUi(
            title = stringResource(Res.string.info_wind_setup_wind_section_title),
            body = stringResource(Res.string.info_wind_setup_wind_section_body),
        ),
        InfoSectionUi(
            title = stringResource(Res.string.info_wind_setup_strength_section_title),
            body = stringResource(Res.string.info_wind_setup_strength_section_body),
        ),
    )
    ScreenInfoModalBottomSheetComponent(
        visible = infoSheetVisible,
        onDismissRequest = { infoSheetVisible = false },
        sheetTitle = stringResource(Res.string.screen_field_info_title),
        sections = windInfoSections,
    )
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.screen_wind_setup_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
            ) {
                Text(
                    text = stringResource(Res.string.wind_setup_btn_save),
                    fontWeight = FontWeight.Bold,
                )
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
            TitleComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.label_wind_direction),
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
                titleTextStyle = MaterialTheme.typography.titleMedium,
            )

            WindDirectionCardComponent(
                windDirectionDegrees = windDirectionDegrees,
                windStrengthPercent = windStrengthPercent,
                windDirection = windDirection,
                isHeadingLoaded = isHeadingLoaded,
                headingForGesture = headingForGesture,
                heading = heading,
                onWindDegreesChange = onWindDegreesChange,
            )

            TitleComponent(
                sectionTitle = SectionTitleUi(
                    title = stringResource(Res.string.label_wind_strength),
                    infoSheetTitle = "",
                    infoDescription = "",
                ),
                showInfoButton = false,
                leadingIcon = {
                    Icon(
                        painter = painterResource(windIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = AppColors.primary,
                    )
                },
                titleTextStyle = MaterialTheme.typography.titleMedium,
            )

            WindStrengthCardComponent(
                windStrengthPercent = windStrengthPercent,
                onWindStrengthChange = onWindStrengthChange,
            )
        }
    }
}
