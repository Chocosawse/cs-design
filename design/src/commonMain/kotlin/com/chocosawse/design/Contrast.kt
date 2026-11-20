package com.chocosawse.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min

/**
 * Checks this palette's light and dark themes against WCAG AA (4.5:1) and returns one message per failing
 * role pair, or an empty list if it passes. Checked: text and secondary text on background, surface,
 * secondaryContainer and surfaceContainerHighest; error on background and surface; onPrimary on primary;
 * onError on error.
 *
 * Apps can test their own palettes with `assertEquals(emptyList(), palette.contrastIssues())`.
 */
fun ColorPalette.contrastIssues(): List<String> = listOf(false, true).flatMap { isDark ->
    val scheme = toMaterialColorScheme(isDark)
    val theme = "$name ${if (isDark) "dark" else "light"}"
    val backgrounds = listOf(
        "background" to scheme.background,
        "surface" to scheme.surface,
        "secondaryContainer" to scheme.secondaryContainer,
        "surfaceContainerHighest" to scheme.surfaceContainerHighest
    )
    val pairs = backgrounds.flatMap { (bgName, bg) ->
        listOf(Triple("text", scheme.onSurface, bgName to bg), Triple("secondary text", scheme.onSurfaceVariant, bgName to bg))
    } + listOf(
        Triple("error", scheme.error, "background" to scheme.background),
        Triple("error", scheme.error, "surface" to scheme.surface),
        Triple("onPrimary", scheme.onPrimary, "primary" to scheme.primary),
        Triple("onError", scheme.onError, "error" to scheme.error)
    )
    // Contrast is only meaningful against an opaque background
    backgrounds.filter { it.second.alpha < 1f }.map { "$theme: ${it.first} is not opaque (alpha ${it.second.alpha})" } +
        pairs.mapNotNull { (fgName, fg, bgPair) ->
            val ratio = contrastRatio(fg, bgPair.second)
            if (ratio < 4.5) "$theme: $fgName on ${bgPair.first} is ${ratio.format()}:1, needs 4.5:1" else null
        }
}

// WCAG 2 contrast ratio, from 1 (identical) to 21 (black on white)
internal fun contrastRatio(foreground: Color, background: Color): Double {
    val lighter = max(foreground.luminance(), background.luminance())
    val darker = min(foreground.luminance(), background.luminance())
    return (lighter + 0.05) / (darker + 0.05)
}

private fun Double.format(): String = ((this * 100).toInt() / 100.0).toString()
