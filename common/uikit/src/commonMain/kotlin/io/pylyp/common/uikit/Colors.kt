package io.pylyp.common.uikit

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val LightPrimary = Color(0xFF825500)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFFFFDDB3)
private val LightOnPrimaryContainer = Color(0xFF291800)
private val LightSecondary = Color(0xFF705B40)
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFFFBDDBB)
private val LightOnSecondaryContainer = Color(0xFF251A04)
private val LightTertiary = Color(0xFF516440)
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightError = Color(0xFFBA1A1A)
private val LightOnError = Color(0xFFFFFFFF)
private val LightErrorContainer = Color(0xFFFFDAD6)
private val LightOnErrorContainer = Color(0xFF410002)
private val LightBackground = Color(0xFFFFFBFF)
private val LightOnBackground = Color(0xFF1F1B16)
private val LightSurface = Color(0xFFFFFBFF)
private val LightOnSurface = Color(0xFF1F1B16)
private val LightSurfaceVariant = Color(0xFFEFE0CF)
private val LightOnSurfaceVariant = Color(0xFF4F4539)
private val LightOutline = Color(0xFF817567)
private val LightOutlineVariant = Color(0xFFD3C4B4)

// Dark theme colors (Dark Roast)
private val DarkPrimary = Color(0xFFFFB951)
private val DarkOnPrimary = Color(0xFF452B00)
private val DarkPrimaryContainer = Color(0xFF633F00)
private val DarkOnPrimaryContainer = Color(0xFFFFDDB3)
private val DarkSecondary = Color(0xFFDDC2A1)
private val DarkOnSecondary = Color(0xFF3E2D16)
private val DarkSecondaryContainer = Color(0xFF56442A)
private val DarkOnSecondaryContainer = Color(0xFFFBDDBB)
private val DarkTertiary = Color(0xFFB8CEA1)
private val DarkOnTertiary = Color(0xFF243515)
private val DarkError = Color(0xFFFFB4AB)
private val DarkOnError = Color(0xFF690005)
private val DarkErrorContainer = Color(0xFF93000A)
private val DarkOnErrorContainer = Color(0xFFFFDAD6)
private val DarkBackground = Color(0xFF1F1B16)
private val DarkOnBackground = Color(0xFFEAE1D9)
private val DarkSurface = Color(0xFF1F1B16)
private val DarkOnSurface = Color(0xFFEAE1D9)
private val DarkSurfaceVariant = Color(0xFF4F4539)
private val DarkOnSurfaceVariant = Color(0xFFD3C4B4)
private val DarkOutline = Color(0xFF9C8F80)
private val DarkOutlineVariant = Color(0xFF4F4539)

internal val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

internal val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)