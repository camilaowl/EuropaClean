package com.ca.europaclean.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class EuropaCleanSpacing(
    val extraSmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val extraLarge: Dp,
    val extraExtraLarge: Dp,
)

internal val DefaultEuropaCleanSpacing = EuropaCleanSpacing(
    extraSmall = 4.dp,
    small = 8.dp,
    medium = 16.dp,
    large = 24.dp,
    extraLarge = 32.dp,
    extraExtraLarge = 48.dp,
)

internal val LocalEuropaCleanSpacing = staticCompositionLocalOf<EuropaCleanSpacing> {
    error("No EuropaCleanSpacing provided")
}
