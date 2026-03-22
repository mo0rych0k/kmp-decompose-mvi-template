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
import io.pylyp.weather.domain.entity.WindDirectionDD
import org.jetbrains.compose.resources.stringResource

@Composable
public fun windDirectionDisplayName(dir: WindDirectionDD): String {
    return when (dir) {
        WindDirectionDD.NORTH -> stringResource(Res.string.wind_north)
        WindDirectionDD.NORTH_EAST -> stringResource(Res.string.wind_northeast)
        WindDirectionDD.EAST -> stringResource(Res.string.wind_east)
        WindDirectionDD.SOUTH_EAST -> stringResource(Res.string.wind_southeast)
        WindDirectionDD.SOUTH -> stringResource(Res.string.wind_south)
        WindDirectionDD.SOUTH_WEST -> stringResource(Res.string.wind_southwest)
        WindDirectionDD.WEST -> stringResource(Res.string.wind_west)
        WindDirectionDD.NORTH_WEST -> stringResource(Res.string.wind_northwest)
    }
}
