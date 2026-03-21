package io.pylyp.weather.ui.skytrack.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.model.InfoSectionUi

@Composable
private fun ScreenInfoSectionsContent(
    sheetTitle: String,
    sections: List<InfoSectionUi>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
    ) {
        Text(
            text = sheetTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(16.dp))
        sections.forEachIndexed { index, section ->
            if (index > 0) {
                Spacer(modifier = Modifier.height(20.dp))
            }
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = section.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenInfoModalBottomSheetComponent(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    sheetTitle: String,
    sections: List<InfoSectionUi>,
) {
    if (!visible) return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        ScreenInfoSectionsContent(sheetTitle = sheetTitle, sections = sections)
    }
}

@Preview
@Composable
internal fun ScreenInfoModalBottomSheetComponentPreview() {
    AppTheme {
        ScreenInfoSectionsContent(
            sheetTitle = "Help",
            sections = listOf(
                InfoSectionUi(title = "A", body = "Body A"),
                InfoSectionUi(title = "B", body = "Body B"),
            ),
        )
    }
}
