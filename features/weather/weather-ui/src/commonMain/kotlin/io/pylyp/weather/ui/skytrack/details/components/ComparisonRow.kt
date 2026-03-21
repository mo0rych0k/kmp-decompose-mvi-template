package io.pylyp.weather.ui.skytrack.details.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi

@Composable
internal fun ComparisonRowComponent(
    label: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    user: String,
    api: String,
    delta: String?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TitleComponent(
            sectionTitle = label,
            leadingIcon = leadingIcon,
            titleTextStyle = MaterialTheme.typography.bodySmall,
            titleFontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(),
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
internal fun ComparisonRowWithUserContentComponent(
    label: SectionTitleUi?,
    labelLeadingIcon: @Composable (() -> Unit)? = null,
    userContent: @Composable () -> Unit,
    api: String,
    delta: String?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            TitleComponent(
                sectionTitle = label,
                leadingIcon = { labelLeadingIcon?.invoke() },
                titleTextStyle = MaterialTheme.typography.bodySmall,
                titleFontWeight = FontWeight.Normal,
                modifier = Modifier.fillMaxWidth(),
            )
        }
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

@Preview
@Composable
internal fun ComparisonRowComponentPreview() {
    AppTheme {
        ComparisonRowComponent(
            label = SectionTitleUi(
                title = "Temperature",
                infoSheetTitle = "Temperature",
                infoDescription = "Compare readings.",
            ),
            leadingIcon = {},
            user = "+5°C",
            api = "+3°C",
            delta = "2°",
        )
    }
}

@Preview
@Composable
internal fun ComparisonRowWithUserContentComponentPreview() {
    AppTheme {
        ComparisonRowWithUserContentComponent(
            label = SectionTitleUi(
                title = "Wind",
                infoSheetTitle = "Wind",
                infoDescription = "Direction and API text.",
            ),
            userContent = { Text("NE", color = AppColors.userDataAccent) },
            api = "10 km/h",
            delta = null,
        )
    }
}
