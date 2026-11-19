package com.chocosawse.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertTrue

class AppThemesTest {

    // Every built-in palette is checked; new ones are picked up from ColorPalette.builtIns
    private val palettes = ColorPalette.builtIns

    @Test
    fun `text meets WCAG AA contrast on background and surface`() = assertAllThemes { name, colors ->
        listOf("background" to colors.background, "surface" to colors.surface).flatMap { (bgName, bg) ->
            listOfNotNull(
                checkOpaque(bg, "$name $bgName"),
                checkContrast(4.5, colors.textPrimary, bg, "$name textPrimary on $bgName"),
                checkContrast(4.5, colors.textSecondary, bg, "$name textSecondary on $bgName")
            )
        }
    }

    @Test
    fun `brand color meets WCAG non-text contrast on background and surface`() = assertAllThemes { name, colors ->
        listOfNotNull(
            checkContrast(3.0, colors.brandPrimary, colors.background, "$name brandPrimary on background"),
            checkContrast(3.0, colors.brandPrimary, colors.surface, "$name brandPrimary on surface")
        )
    }

    // Runs checks for every palette in light and dark, then reports all failures together
    private fun assertAllThemes(checks: (String, AppColors) -> List<String>) {
        val failures = palettes.flatMap { palette ->
            listOf(false, true).flatMap { isDark ->
                checks("${palette.name} ${if (isDark) "dark" else "light"}", palette.toAppColors(isDark))
            }
        }
        assertTrue(failures.isEmpty(), "\n" + failures.joinToString("\n"))
    }

    // Contrast is only meaningful against an opaque background
    private fun checkOpaque(color: Color, label: String): String? =
        if (color.alpha < 1f) "$label is not opaque (alpha ${color.alpha})" else null

    private fun checkContrast(minimum: Double, foreground: Color, background: Color, label: String): String? {
        val lighter = max(foreground.luminance(), background.luminance())
        val darker = min(foreground.luminance(), background.luminance())
        val ratio = (lighter + 0.05) / (darker + 0.05)
        return if (ratio < minimum) "$label contrast is ${ratio.toString().take(4)}, needs $minimum" else null
    }
}
