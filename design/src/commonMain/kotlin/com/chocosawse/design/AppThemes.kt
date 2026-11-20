package com.chocosawse.design

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.isSystemInDarkTheme

data class AppColors(
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val brandPrimary: Color,
    // Not contrast-safe on background in every theme (e.g. Viva Magenta light, Emerald dark); use for fills, not text
    val brandAccent: Color,
    // Tinted fill for chips, selected nav items and tonal buttons (Material's secondaryContainer)
    val secondaryContainer: Color,
    // Error text and icons; contrast-checked against background and surface
    val error: Color,
    val isDark: Boolean
)

private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    error("AppTheme colors have not been provided!")
}

@Composable
fun AppTheme(
    activeToken: ColorPalette = ColorPalette.PantoneVivaMagenta, // Can be swapped dynamically at runtime
    darkTheme: Boolean = isSystemInDarkTheme(),
    animateChanges: Boolean = true,
    typography: Typography = MaterialTheme.typography,
    shapes: Shapes = MaterialTheme.shapes,
    content: @Composable () -> Unit
) {
    val semanticColors = activeToken.toAppColors(isDark = darkTheme)
    val newScheme = activeToken.toMaterialColorScheme(isDark = darkTheme)

    MaterialTheme(
        colorScheme =
            if (animateChanges) {
                animateColorSchemeAsState(
                    targetColorScheme = newScheme,
                    animationSpec = tween(durationMillis = 300))
            } else {
                newScheme
            },
        typography = typography,
        shapes = shapes
    ) {
        CompositionLocalProvider(
            LocalAppColors provides semanticColors,
            // Default color for Text/Icon outside a Surface, which MaterialTheme alone leaves black
            LocalContentColor provides semanticColors.textPrimary,
            content = content
        )
    }
}

@Composable
fun animateColorSchemeAsState(
    targetColorScheme: ColorScheme,
    animationSpec: AnimationSpec<Color> = tween(durationMillis = 300)
): ColorScheme {
    return ColorScheme(
        primary = animateColorAsState(
            targetColorScheme.primary,
            animationSpec,
            label = "primary"
        ).value,
        onPrimary = animateColorAsState(
            targetColorScheme.onPrimary,
            animationSpec,
            label = "onPrimary"
        ).value,
        primaryContainer = animateColorAsState(
            targetColorScheme.primaryContainer,
            animationSpec,
            label = "primaryContainer"
        ).value,
        onPrimaryContainer = animateColorAsState(
            targetColorScheme.onPrimaryContainer,
            animationSpec,
            label = "onPrimaryContainer"
        ).value,
        inversePrimary = animateColorAsState(
            targetColorScheme.inversePrimary,
            animationSpec,
            label = "inversePrimary"
        ).value,
        secondary = animateColorAsState(
            targetColorScheme.secondary,
            animationSpec,
            label = "secondary"
        ).value,
        onSecondary = animateColorAsState(
            targetColorScheme.onSecondary,
            animationSpec,
            label = "onSecondary"
        ).value,
        secondaryContainer = animateColorAsState(
            targetColorScheme.secondaryContainer,
            animationSpec,
            label = "secondaryContainer"
        ).value,
        onSecondaryContainer = animateColorAsState(
            targetColorScheme.onSecondaryContainer,
            animationSpec,
            label = "onSecondaryContainer"
        ).value,
        tertiary = animateColorAsState(
            targetColorScheme.tertiary,
            animationSpec,
            label = "tertiary"
        ).value,
        onTertiary = animateColorAsState(
            targetColorScheme.onTertiary,
            animationSpec,
            label = "onTertiary"
        ).value,
        tertiaryContainer = animateColorAsState(
            targetColorScheme.tertiaryContainer,
            animationSpec,
            label = "tertiaryContainer"
        ).value,
        onTertiaryContainer = animateColorAsState(
            targetColorScheme.onTertiaryContainer,
            animationSpec,
            label = "onTertiaryContainer"
        ).value,
        background = animateColorAsState(
            targetColorScheme.background,
            animationSpec,
            label = "background"
        ).value,
        onBackground = animateColorAsState(
            targetColorScheme.onBackground,
            animationSpec,
            label = "onBackground"
        ).value,
        surface = animateColorAsState(
            targetColorScheme.surface,
            animationSpec,
            label = "surface"
        ).value,
        onSurface = animateColorAsState(
            targetColorScheme.onSurface,
            animationSpec,
            label = "onSurface"
        ).value,
        surfaceVariant = animateColorAsState(
            targetColorScheme.surfaceVariant,
            animationSpec,
            label = "surfaceVariant"
        ).value,
        onSurfaceVariant = animateColorAsState(
            targetColorScheme.onSurfaceVariant,
            animationSpec,
            label = "onSurfaceVariant"
        ).value,
        surfaceTint = animateColorAsState(
            targetColorScheme.surfaceTint,
            animationSpec,
            label = "surfaceTint"
        ).value,
        inverseSurface = animateColorAsState(
            targetColorScheme.inverseSurface,
            animationSpec,
            label = "inverseSurface"
        ).value,
        inverseOnSurface = animateColorAsState(
            targetColorScheme.inverseOnSurface,
            animationSpec,
            label = "inverseOnSurface"
        ).value,
        error = animateColorAsState(targetColorScheme.error, animationSpec, label = "error").value,
        onError = animateColorAsState(
            targetColorScheme.onError,
            animationSpec,
            label = "onError"
        ).value,
        errorContainer = animateColorAsState(
            targetColorScheme.errorContainer,
            animationSpec,
            label = "errorContainer"
        ).value,
        onErrorContainer = animateColorAsState(
            targetColorScheme.onErrorContainer,
            animationSpec,
            label = "onErrorContainer"
        ).value,
        outline = animateColorAsState(
            targetColorScheme.outline,
            animationSpec,
            label = "outline"
        ).value,
        outlineVariant = animateColorAsState(
            targetColorScheme.outlineVariant,
            animationSpec,
            label = "outlineVariant"
        ).value,
        scrim = animateColorAsState(targetColorScheme.scrim, animationSpec, label = "scrim").value,
        surfaceContainer = animateColorAsState(
            targetColorScheme.surfaceContainer,
            animationSpec,
            label = "surfaceContainer"
        ).value,
        surfaceContainerHigh = animateColorAsState(
            targetColorScheme.surfaceContainerHigh,
            animationSpec,
            label = "surfaceContainerHigh"
        ).value,
        surfaceContainerHighest = animateColorAsState(
            targetColorScheme.surfaceContainerHighest,
            animationSpec,
            label = "surfaceContainerHighest"
        ).value,
        surfaceContainerLow = animateColorAsState(
            targetColorScheme.surfaceContainerLow,
            animationSpec,
            label = "surfaceContainerLow"
        ).value,
        surfaceContainerLowest = animateColorAsState(
            targetColorScheme.surfaceContainerLowest,
            animationSpec,
            label = "surfaceContainerLowest"
        ).value,
        surfaceDim = animateColorAsState(
            targetColorScheme.surfaceDim,
            animationSpec,
            label = "surfaceDim"
        ).value,
        surfaceBright = animateColorAsState(
            targetColorScheme.surfaceBright,
            animationSpec,
            label = "surfaceBright"
        ).value,
        primaryFixed = animateColorAsState(
            targetColorScheme.primaryFixed,
            animationSpec,
            label = "primaryFixed"
        ).value,
        primaryFixedDim = animateColorAsState(
            targetColorScheme.primaryFixedDim,
            animationSpec,
            label = "primaryFixedDim"
        ).value,
        secondaryFixed = animateColorAsState(
            targetColorScheme.secondaryFixed,
            animationSpec,
            label = "secondaryFixed"
        ).value,
        secondaryFixedDim = animateColorAsState(
            targetColorScheme.secondaryFixedDim,
            animationSpec,
            label = "secondaryFixedDim"
        ).value,
        tertiaryFixed = animateColorAsState(
            targetColorScheme.tertiaryFixed,
            animationSpec,
            label = "tertiaryFixed"
        ).value,
        tertiaryFixedDim = animateColorAsState(
            targetColorScheme.tertiaryFixedDim,
            animationSpec,
            label = "tertiaryFixedDim"
        ).value,
        onPrimaryFixed = animateColorAsState(
            targetColorScheme.onPrimaryFixed,
            animationSpec,
            label = "onPrimaryFixed"
        ).value,
        onPrimaryFixedVariant = animateColorAsState(
            targetColorScheme.onPrimaryFixedVariant,
            animationSpec,
            label = "onPrimaryFixedVariant"
        ).value,
        onSecondaryFixed = animateColorAsState(
            targetColorScheme.onSecondaryFixed,
            animationSpec,
            label = "onSecondaryFixed"
        ).value,
        onSecondaryFixedVariant = animateColorAsState(
            targetColorScheme.onSecondaryFixedVariant,
            animationSpec,
            label = "onSecondaryFixedVariant"
        ).value,
        onTertiaryFixed = animateColorAsState(
            targetColorScheme.onTertiaryFixed,
            animationSpec,
            label = "onTertiaryFixed"
        ).value,
        onTertiaryFixedVariant = animateColorAsState(
            targetColorScheme.onTertiaryFixedVariant,
            animationSpec,
            label = "onTertiaryFixedVariant"
        ).value
    )
}

/**
 * Feeds the palette into Material 3 so Material components (Button, Card, TextField...) follow it.
 * Every role is derived from the palette except primaryContainer, tertiary*, errorContainer, the *Fixed roles
 * and scrim, which keep Material's baseline values.
 */
internal fun ColorPalette.toMaterialColorScheme(isDark: Boolean): ColorScheme {
    val colors = toAppColors(isDark)
    val surface = colors.surface
    // Containers step from the surface toward a mid shade: darker in light mode, lighter in dark mode
    val containerTarget = if (isDark) color700 else color300
    fun container(step: Float) = lerp(surface, containerTarget, step)
    val surfaceContainerHighest = container(0.24f)
    val baseline = if (isDark) darkColorScheme() else lightColorScheme()
    return baseline.copy(
        primary = colors.brandPrimary,
        onPrimary = readableOn(colors.brandPrimary),
        // Tints elevated surfaces with the brand colour
        surfaceTint = colors.brandPrimary,
        // The other mode's primary, so it stands out on inverseSurface
        inversePrimary = if (isDark) primary else primaryNight,
        secondary = colors.brandAccent,
        onSecondary = readableOn(colors.brandAccent),
        secondaryContainer = colors.secondaryContainer,
        onSecondaryContainer = readableOn(colors.secondaryContainer),
        background = colors.background,
        onBackground = colors.textPrimary,
        surface = surface,
        onSurface = colors.textPrimary,
        onSurfaceVariant = colors.textSecondary,
        // Same as surfaceContainerHighest, as in Material's baseline
        surfaceVariant = surfaceContainerHighest,
        // Lowest is the surface itself; each level is a small step further toward containerTarget
        surfaceContainerLowest = surface,
        surfaceContainerLow = container(0.06f),
        surfaceContainer = container(0.12f),
        surfaceContainerHigh = container(0.18f),
        surfaceContainerHighest = surfaceContainerHighest,
        // The surface is the bright end in light mode and the dim end in dark mode
        surfaceDim = if (isDark) surface else container(0.3f),
        surfaceBright = if (isDark) container(0.3f) else surface,
        // Snackbars and tooltips: the opposite end of the palette
        inverseSurface = if (isDark) color100 else color900,
        inverseOnSurface = if (isDark) color900 else color100,
        // Mid shade for borders; outlineVariant is a subtler divider (color300 in light, half a step from the
        // dark surface toward color700 in dark)
        outline = color500,
        outlineVariant = if (isDark) container(0.5f) else color300,
        error = colors.error,
        onError = readableOn(colors.error),
    )
}

// Black or white, whichever has more contrast against color (0.179 is the WCAG luminance crossover)
internal fun readableOn(color: Color): Color =
    if (color.luminance() > 0.179f) Color.Black else Color.White
