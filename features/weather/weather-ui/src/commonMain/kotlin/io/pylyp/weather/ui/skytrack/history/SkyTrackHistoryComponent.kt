package io.pylyp.weather.ui.skytrack.history

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import io.pylyp.common.core.di.ComponentFactory
import io.pylyp.core.navigation.asValue
import io.pylyp.core.navigation.subscribe
import io.pylyp.weather.ui.di.createSkyTrackHistoryStore
import io.pylyp.weather.ui.skytrack.history.store.SkyTrackHistoryStore
import io.pylyp.weather.ui.skytrack.model.ObservationCalendarDayUi
import org.koin.core.component.KoinComponent
import kotlinx.coroutines.CoroutineScope

internal interface SkyTrackHistoryComponent {
    val state: Value<SkyTrackHistoryStore.State>
    fun onIntent(intent: SkyTrackHistoryStore.Intent)
}

internal class DefaultSkyTrackHistoryComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    initialSelectedDay: ObservationCalendarDayUi?,
    private val output: (Output) -> Unit,
) : SkyTrackHistoryComponent, ComponentContext by componentContext, KoinComponent {

    private val store: SkyTrackHistoryStore = instanceKeeper.getStore {
        componentFactory.createSkyTrackHistoryStore(initialSelectedDay = initialSelectedDay)
    }

    private val componentScope: CoroutineScope = coroutineScope()

    override val state: Value<SkyTrackHistoryStore.State> = store.asValue()

    private val backCallback = BackCallback {
        store.accept(SkyTrackHistoryStore.Intent.BackIntent)
    }

    init {
        backHandler.register(backCallback)
        store.subscribe(scope = componentScope) { label ->
            when (label) {
                SkyTrackHistoryStore.Label.BackLabel -> output(Output.Finished)
                SkyTrackHistoryStore.Label.OpenAddLabel -> output(Output.OpenAdd)
                is SkyTrackHistoryStore.Label.GoToTodayLabel ->
                    output(Output.GoToToday(today = label.today))
                is SkyTrackHistoryStore.Label.OpenCalendarLabel ->
                    output(Output.OpenCalendar(focusDay = label.focusDay))
                is SkyTrackHistoryStore.Label.OpenDetailsLabel -> output(Output.OpenDetails(recordId = label.recordId))
            }
        }
    }

    override fun onIntent(intent: SkyTrackHistoryStore.Intent) {
        store.accept(intent)
    }

    sealed interface Output {
        data object Finished : Output
        data object OpenAdd : Output
        data class GoToToday(val today: ObservationCalendarDayUi) : Output
        data class OpenCalendar(val focusDay: ObservationCalendarDayUi) : Output
        data class OpenDetails(val recordId: Long) : Output
    }
}
