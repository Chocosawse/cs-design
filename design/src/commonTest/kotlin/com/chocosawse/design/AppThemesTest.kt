package com.chocosawse.design

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppThemesTest {

    // Every built-in palette is checked; new ones are picked up from ColorPalette.builtIns
    private val palettes = ColorPalette.builtIns

    @Test
    fun `built-in palettes meet WCAG AA text contrast`() {
        val issues = palettes.flatMap { it.contrastIssues() }
        assertTrue(issues.isEmpty(), "\n" + issues.joinToString("\n"))
    }

    @Test
    fun `brand color meets WCAG non-text contrast on background and surface`() {
        val failures = palettes.flatMap { palette ->
            listOf(false, true).flatMap { isDark ->
                val colors = palette.toAppColors(isDark)
                val theme = "${palette.name} ${if (isDark) "dark" else "light"}"
                listOf("background" to colors.background, "surface" to colors.surface).mapNotNull { (bgName, bg) ->
                    val ratio = contrastRatio(colors.brandPrimary, bg)
                    if (ratio < 3.0) "$theme brandPrimary on $bgName contrast is $ratio, needs 3.0" else null
                }
            }
        }
        assertTrue(failures.isEmpty(), "\n" + failures.joinToString("\n"))
    }

    @Test
    fun `contrastIssues reports low-contrast text`() {
        // Mid-grey shades everywhere, so text vanishes into the background
        val grey = Color(0xFF808080)
        val palette = ColorPalette("Grey", grey, grey, grey, grey, grey, grey, grey, grey)
        assertTrue(palette.contrastIssues().any { it.startsWith("Grey light: text on background") })
    }

        @Test
    fun `Newspaper dark background is ink black`() {
        assertEquals(Color(0xFF141414), ColorPalette.Newspaper.toMaterialColorScheme(isDark = true).background)
    }

    // Values from 0.3.0, before palettes could set surfaces and errors; these roles must not change
    @Test
    fun `palettes from before 0_4_0 keep their core colours`() {
        data class Core(
            val primary: Long, val background: Long, val surface: Long, val onSurface: Long,
            val onSurfaceVariant: Long, val secondary: Long, val secondaryContainer: Long
        )
        val expected = mapOf(
            (ColorPalette.PantoneVivaMagenta to false) to
                Core(0xFFBB2649, 0xFFF3CED7, 0xFFFFFFFF, 0xFF2C0710, 0xFF711329, 0xFF26BA98, 0xFFF3CED7),
            (ColorPalette.PantoneVivaMagenta to true) to
                Core(0xFFEC7490, 0xFF2C0710, 0xFF4D0D1C, 0xFFFFFFFF, 0xFFF3CED7, 0xFF26BA98, 0xFF2C0710),
            (ColorPalette.PantoneEmerald to false) to
                Core(0xFF009473, 0xFFC4FDF1, 0xFFFFFFFF, 0xFF002920, 0xFF005240, 0xFF940020, 0xFFC4FDF1),
            (ColorPalette.PantoneEmerald to true) to
                Core(0xFF26EDC2, 0xFF002920, 0xFF003D30, 0xFFFFFFFF, 0xFFC4FDF1, 0xFF940020, 0xFF002920),
            (ColorPalette.PantoneClassicBlue to false) to
                Core(0xFF0F4C81, 0xFFCAE2F7, 0xFFFFFFFF, 0xFF031626, 0xFF072B4B, 0xFF80440F, 0xFFCAE2F7),
            (ColorPalette.PantoneClassicBlue to true) to
                Core(0xFF218EED, 0xFF031626, 0xFF052038, 0xFFFFFFFF, 0xFFCAE2F7, 0xFF80440F, 0xFF031626),
        )
        expected.forEach { (key, core) ->
            val (palette, isDark) = key
            val s = palette.toMaterialColorScheme(isDark)
            val label = "${palette.name} ${if (isDark) "dark" else "light"}"
            assertEquals(Color(core.primary), s.primary, "$label primary")
            assertEquals(Color(core.background), s.background, "$label background")
            assertEquals(Color(core.surface), s.surface, "$label surface")
            assertEquals(Color(core.onSurface), s.onSurface, "$label onSurface")
            assertEquals(Color(core.onSurfaceVariant), s.onSurfaceVariant, "$label onSurfaceVariant")
            assertEquals(Color(core.secondary), s.secondary, "$label secondary")
            assertEquals(Color(core.secondaryContainer), s.secondaryContainer, "$label secondaryContainer")
        }
    }
}
