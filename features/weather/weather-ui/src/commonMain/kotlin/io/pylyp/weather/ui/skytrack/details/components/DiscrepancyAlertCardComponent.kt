package io.pylyp.weather.ui.skytrack.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppColors
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.components.TitleComponent
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi

@Composable
internal fun DiscrepancyAlertCardComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    alertText: String,
    modifier: Modifier = Modifier,
) {
    TitleComponent(
        sectionTitle = sectionTitle,
        leadingIcon = leadingIcon,
        modifier = Modifier.fillMaxWidth(),
    )
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.error.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = alertText,
            color = AppColors.error,
            modifier = Modifier.padding(12.dp),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
internal fun DiscrepancyAlertCardComponentPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DiscrepancyAlertCardComponent(
                sectionTitle = SectionTitleUi(
                    title = "Large difference",
                    infoSheetTitle = "Large difference",
                    infoDescription = "Your values differ from API.",
                ),
                leadingIcon = {},
                alertText = "Large difference between your observation and API data.",
            )
        }
    }
}
