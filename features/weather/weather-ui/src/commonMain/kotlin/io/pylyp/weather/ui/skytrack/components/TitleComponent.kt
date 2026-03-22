package io.pylyp.weather.ui.skytrack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.a11y_section_info
import io.pylyp.common.uikit.AppTheme
import io.pylyp.weather.ui.skytrack.model.SectionTitleUi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TitleComponent(
    sectionTitle: SectionTitleUi,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    titleFontWeight: FontWeight = FontWeight.Bold,
    showInfoButton: Boolean = true,
) {
    var sheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showInfoButton && sheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { sheetVisible = false },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                Text(
                    text = sectionTitle.infoSheetTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = sectionTitle.infoDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        leadingIcon()
        Text(
            text = sectionTitle.title,
            style = titleTextStyle,
            fontWeight = titleFontWeight,
            modifier = Modifier.weight(1f),
        )
        if (showInfoButton) {
            IconButton(
                onClick = { sheetVisible = true },
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(Res.string.a11y_section_info),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun TitleComponentPreview() {
    AppTheme {
        TitleComponent(
            sectionTitle = SectionTitleUi(
                title = "Temperature",
                infoSheetTitle = "Temperature",
                infoDescription = "Your reading compared to the API.",
            ),
            leadingIcon = {},
        )
    }
}
