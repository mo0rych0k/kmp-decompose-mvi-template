package io.pylyp.weather.ui.skytrack.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
internal fun ComparisonColumnsHeaderComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    myObservationsLabel: String,
    apiDataLabel: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TitleComponent(
            sectionTitle = sectionTitle,
            leadingIcon = leadingIcon,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
            Text(
                text = myObservationsLabel,
                modifier = Modifier
                    .weight(1f)
                    .background(AppColors.userDataAccent)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = apiDataLabel,
                modifier = Modifier
                    .weight(1f)
                    .background(AppColors.apiDataAccent)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
internal fun ComparisonColumnsHeaderComponentPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ComparisonColumnsHeaderComponent(
                sectionTitle = SectionTitleUi(
                    title = "Comparison",
                    infoSheetTitle = "Comparison",
                    infoDescription = "Left vs right columns.",
                ),
                leadingIcon = {},
                myObservationsLabel = "My observations",
                apiDataLabel = "API data",
            )
        }
    }
}
