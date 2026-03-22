package io.pylyp.weather.ui.skytrack.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.action_share
import io.pylyp.common.resources.observation_calendar_month_subtitle
import io.pylyp.common.resources.observation_calendar_title
import io.pylyp.weather.ui.skytrack.calendar.components.ObservationCalendarGridComponent
import io.pylyp.weather.ui.skytrack.calendar.components.ObservationCalendarGridUi
import io.pylyp.weather.ui.skytrack.calendar.store.SkyTrackCalendarStore
import org.jetbrains.compose.resources.stringResource
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkyTrackCalendarScreen(
    component: SkyTrackCalendarComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    val monthTitle = remember(state.visibleYear, state.visibleMonth) {
        val d = LocalDate(state.visibleYear, state.visibleMonth, 1)
        val mn = d.month.name
        val label = mn.substring(0, 1).uppercase() + mn.substring(1).lowercase()
        "$label ${state.visibleYear}"
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.observation_calendar_title),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { component.onIntent(SkyTrackCalendarStore.Intent.BackIntent) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { component.onIntent(SkyTrackCalendarStore.Intent.ShareAllIntent) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(Res.string.action_share),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .heightIn(min = 320.dp),
        ) {
            if (state.isLoading && state.countsByDay.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                ObservationCalendarGridComponent(
                    monthTitle = monthTitle,
                    monthSubtitle = stringResource(Res.string.observation_calendar_month_subtitle),
                    grid = ObservationCalendarGridUi(
                        year = state.visibleYear,
                        monthNumber = state.visibleMonth,
                        countsByDay = state.countsByDay,
                        focusDay = state.focusDay,
                    ),
                    onPreviousMonth = {
                        component.onIntent(SkyTrackCalendarStore.Intent.PreviousMonthIntent)
                    },
                    onNextMonth = {
                        component.onIntent(SkyTrackCalendarStore.Intent.NextMonthIntent)
                    },
                    onDayClick = { day ->
                        component.onIntent(SkyTrackCalendarStore.Intent.SelectDayIntent(dayOfMonth = day))
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        }
    }
}
