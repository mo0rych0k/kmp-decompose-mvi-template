package io.pylyp.weather.ui.skytrack.add.wind

import androidx.compose.runtime.Composable
import io.pylyp.common.resources.Res
import io.pylyp.common.resources.wind_east
import io.pylyp.common.resources.wind_north
import io.pylyp.common.resources.wind_northeast
import io.pylyp.common.resources.wind_northwest
import io.pylyp.common.resources.wind_south
import io.pylyp.common.resources.wind_southeast
import io.pylyp.common.resources.wind_southwest
import io.pylyp.common.resources.wind_west
import io.pylyp.weather.ui.skytrack.model.WindDirectionUi
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun windDirectionDisplayName(dir: WindDirectionUi): String {
    return when (dir) {
        WindDirectionUi.NORTH -> stringResource(Res.string.wind_north)
        WindDirectionUi.NORTH_EAST -> stringResource(Res.string.wind_northeast)
        WindDirectionUi.EAST -> stringResource(Res.string.wind_east)
        WindDirectionUi.SOUTH_EAST -> stringResource(Res.string.wind_southeast)
        WindDirectionUi.SOUTH -> stringResource(Res.string.wind_south)
        WindDirectionUi.SOUTH_WEST -> stringResource(Res.string.wind_southwest)
        WindDirectionUi.WEST -> stringResource(Res.string.wind_west)
        WindDirectionUi.NORTH_WEST -> stringResource(Res.string.wind_northwest)
    }
}
