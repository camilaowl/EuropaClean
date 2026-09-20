package com.ca.europaclean.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun EuropaCleanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val dynamicColors = if (dynamicColor) {
        platformDynamicColors(darkTheme)
    } else {
        null
    }
    val colors = dynamicColors ?: if (darkTheme) {
        EuropaCleanDarkColors
    } else {
        EuropaCleanLightColors
    }

    CompositionLocalProvider(
        LocalEuropaCleanColors provides colors,
        LocalEuropaCleanTypography provides DefaultEuropaCleanTypography,
        LocalEuropaCleanShapes provides DefaultEuropaCleanShapes,
        LocalEuropaCleanSpacing provides DefaultEuropaCleanSpacing,
        LocalEuropaCleanElevations provides DefaultEuropaCleanElevations,
        content = content
    )
}

object Theme {
    val colors: EuropaCleanColors
        @Composable
        @ReadOnlyComposable
        get() = LocalEuropaCleanColors.current

    val typography: EuropaCleanTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalEuropaCleanTypography.current

    val shapes: EuropaCleanShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalEuropaCleanShapes.current

    val spacing: EuropaCleanSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalEuropaCleanSpacing.current

    val elevations: EuropaCleanElevations
        @Composable
        @ReadOnlyComposable
        get() = LocalEuropaCleanElevations.current
}

@Composable
internal expect fun platformDynamicColors(darkTheme: Boolean): EuropaCleanColors?
