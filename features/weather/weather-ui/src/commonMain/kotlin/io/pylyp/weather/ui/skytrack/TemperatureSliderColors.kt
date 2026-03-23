package io.pylyp.weather.ui.skytrack

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlin.math.abs

private val TemperatureColdBlue: Color = Color(0xFF1D4ED8)
private val TemperatureNeutralWhite: Color = Color.White

/** Darker shade for 0 °C in light theme — visible against light background. */
private val TemperatureNeutralLightTheme: Color = Color(0xFF64748B)
private val TemperatureHotRed: Color = Color(0xFFDC2626)

private const val SpanEpsilon: Float = 1e-4f
private const val LerpDenomEpsilon: Float = 1e-5f

/**
 * Slider accent: blue at [minC], neutral at 0 °C, red at [maxC] (linear in two segments).
 * Use [isLightTheme] = true to get a darker neutral (0x64748B) for visibility on light backgrounds.
 */
internal fun temperatureSliderAccentColor(
    valueCelsius: Double,
    minC: Float = -30f,
    maxC: Float = 45f,
    isLightTheme: Boolean = false,
): Color {
    val neutral = if (isLightTheme) TemperatureNeutralLightTheme else TemperatureNeutralWhite
    val span = maxC - minC
    if (!span.isFinite() || abs(span) < SpanEpsilon) {
        return neutral
    }

    val t = ((valueCelsius.toFloat() - minC) / span).coerceIn(0f, 1f)
    if (!t.isFinite()) {
        return neutral
    }

    val neutralPos = (0f - minC) / span
    if (!neutralPos.isFinite()) {
        return neutral
    }

    // 0 °C at or before the left edge: minC > 0 → only warm side (blue→red); minC == 0 → white at left.
    if (neutralPos <= 0f) {
        return if (minC > 0f) {
            lerp(TemperatureColdBlue, TemperatureHotRed, t)
        } else {
            lerp(neutral, TemperatureHotRed, t)
        }
    }

    // 0 °C at or after the right edge (maxC ≤ 0).
    if (neutralPos >= 1f) {
        return lerp(TemperatureColdBlue, neutral, t)
    }

    // 0 °C strictly between minC and maxC: two segments; keep denominators away from 0.
    val neutralSafe = neutralPos.coerceIn(LerpDenomEpsilon, 1f - LerpDenomEpsilon)
    return if (t <= neutralPos) {
        lerp(
            TemperatureColdBlue,
            neutral,
            (t / neutralSafe).coerceIn(0f, 1f),
        )
    } else {
        val rightSpan = (1f - neutralPos).coerceAtLeast(LerpDenomEpsilon)
        lerp(
            neutral,
            TemperatureHotRed,
            ((t - neutralPos) / rightSpan).coerceIn(0f, 1f),
        )
    }
}
