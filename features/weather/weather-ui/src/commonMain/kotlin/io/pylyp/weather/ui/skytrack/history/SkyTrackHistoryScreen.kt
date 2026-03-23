package io.pylyp.weather.ui.skytrack.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import io.pylyp.common.resources.btn_add_record
import io.pylyp.common.resources.btn_go_to_today
import io.pylyp.common.resources.nav_calendar
import io.pylyp.common.resources.observation_empty_description
import io.pylyp.common.resources.observation_empty_for_day_description
import io.pylyp.common.resources.observation_empty_for_day_title
import io.pylyp.common.resources.observation_empty_title
import io.pylyp.common.resources.observation_history_title
import io.pylyp.common.uikit.AppColors
import io.pylyp.weather.ui.skytrack.history.components.ObservationListItemComponent
import io.pylyp.weather.ui.skytrack.history.store.SkyTrackHistoryStore
import io.pylyp.weather.ui.skytrack.model.formatObservationDayIso
import io.pylyp.weather.ui.skytrack.model.isSameDayAs
import io.pylyp.weather.ui.skytrack.model.todayObservationCalendarDayUi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkyTrackHistoryScreen(
    component: SkyTrackHistoryComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.subscribeAsState()
    val today = remember { todayObservationCalendarDayUi() }
    val isSelectedDayToday = remember(state.selectedDay, today) {
        state.selectedDay.isSameDayAs(today)
    }
    val selectedDayLabel = remember(state.selectedDay) {
        formatObservationDayIso(state.selectedDay)
    }
    val isGlobalEmpty =
        state.records.isEmpty() && !state.isLoading && state.totalObservationsInDatabase == 0
    val isDayEmpty =
        state.records.isEmpty() && !state.isLoading && state.totalObservationsInDatabase > 0

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(Res.string.observation_history_title),
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = selectedDayLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { component.onIntent(SkyTrackHistoryStore.Intent.BackIntent) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { component.onIntent(SkyTrackHistoryStore.Intent.ShareDayIntent) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(Res.string.action_share),
                        )
                    }
                    IconButton(
                        onClick = { component.onIntent(SkyTrackHistoryStore.Intent.OpenCalendarIntent) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = stringResource(Res.string.nav_calendar),
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            if (isSelectedDayToday) {
                FloatingActionButton(
                    onClick = { component.onIntent(SkyTrackHistoryStore.Intent.OpenAddIntent) },
                    containerColor = AppColors.primary,
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = stringResource(Res.string.btn_add_record),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            } else {
                FloatingActionButton(
                    onClick = { component.onIntent(SkyTrackHistoryStore.Intent.GoToTodayIntent) },
                    containerColor = AppColors.primary,
                ) {
                    Icon(
                        Icons.Default.Today,
                        contentDescription = stringResource(Res.string.btn_go_to_today),
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .heightIn(min = 200.dp),
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                isGlobalEmpty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(Res.string.observation_empty_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(Res.string.observation_empty_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                isDayEmpty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(Res.string.observation_empty_for_day_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(Res.string.observation_empty_for_day_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        items(state.records, key = { it.id }) { record ->
                            ObservationListItemComponent(
                                record = record,
                                onClick = {
                                    component.onIntent(SkyTrackHistoryStore.Intent.OpenDetailsIntent(recordId = record.id))
                                },
                                onDelete = {
                                    component.onIntent(SkyTrackHistoryStore.Intent.DeleteRecordIntent(recordId = record.id))
                                },
                                onShare = {
                                    component.onIntent(SkyTrackHistoryStore.Intent.ShareRecordIntent(record = record))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
