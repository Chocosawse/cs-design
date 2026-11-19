package com.chocosawse.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Maps a multi-shade primitive ColorPalette into the semantic AppColors contract.
 * All shade-to-role logic lives exclusively in this mapper; AppThemesTest checks the result meets WCAG contrast.
 */
fun ColorPalette.toAppColors(isDark: Boolean): AppColors {
    return if (!isDark) {
        // --- DAY / LIGHT SCHEME ---
        AppColors(
            brandPrimary = this.primary,
            // color300 is too dark for primary (brand) and color700 (secondary text) to stand out on
            background = this.color100,
            surface = Color(0xFFFFFFFF),
            textPrimary = this.color900,
            textSecondary = this.color700,
            isDark = false,
            brandAccent = this.complement
        )
    } else {
        // --- NIGHT / DARK SCHEME ---
        AppColors(
            brandPrimary = this.primaryNight,
            background = this.color900,
            // Opaque and a step lighter than background; there's no 800 shade, so blend 900 and 700
            surface = lerp(this.color900, this.color700, 0.5f),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = this.color100,
            isDark = true,
            brandAccent = this.complement
        )
    }
}

/**
 * A brand palette. The nested objects are the built-in Pantone palettes; apps can add their own with
 * `ColorPalette(name = ..., primary = ..., ...)` or by subclassing.
 */
open class ColorPalette(
    open val name: String,
    val primary: Color,
    val primaryNight: Color,
    val color100: Color,
    val color300: Color,
    val color500: Color,
    val color700: Color,
    val color900: Color,
    val complement: Color
) {
    companion object {
        /**
         * Every built-in palette, in display order, so apps can offer them as theme choices without
         * listing them by hand. Add new built-in palettes here; AppThemesTest checks everything listed.
         */
        val builtIns: List<ColorPalette> by lazy { listOf(PantoneVivaMagenta, PantoneEmerald, PantoneClassicBlue) }
    }

    object PantoneVivaMagenta : ColorPalette(
        name = "Viva Magenta",
        primary = Colors.pantoneVivaMagentaPrimary,
        primaryNight = Colors.pantoneVivaMagentaNightMode,
        color100 = Colors.pantoneVivaMagenta100,
        color300 = Colors.pantoneVivaMagenta300,
        color500 = Colors.pantoneVivaMagenta500,
        color700 = Colors.pantoneVivaMagenta700,
        color900 = Colors.pantoneVivaMagenta900,
        complement = Colors.pantoneVivaMagentaComplement
    )

    object PantoneEmerald: ColorPalette(
        name = "Emerald",
        primary = Colors.pantoneEmeraldPrimary,
        primaryNight = Colors.pantoneEmeraldNightMode,
        color100 = Colors.pantoneEmerald100,
        color300 = Colors.pantoneEmerald300,
        color500 = Colors.pantoneEmerald500,
        color700 = Colors.pantoneEmerald700,
        color900 = Colors.pantoneEmerald900,
        complement = Colors.pantoneEmeraldComplement
    )

    object PantoneClassicBlue: ColorPalette(
        name = "Classic Blue",
        primary = Color(0xFF0F4C81),
        primaryNight = Color(0xFF218EED),
        color100 = Color(0xFFCAE2F7),
        color300 = Color(0xFF72B1E9),
        color500 = Color(0xFF134B7C),
        color700 = Color(0xFF072B4B),
        color900 = Color(0xFF031626),
        complement = Color(0xFF80440F)
    )
}