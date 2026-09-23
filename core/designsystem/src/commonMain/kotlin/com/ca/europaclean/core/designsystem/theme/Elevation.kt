package com.ca.europaclean.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class EuropaCleanElevations(
    val none: Dp,
    val low: Dp,
    val medium: Dp,
    val high: Dp,
)

internal val DefaultEuropaCleanElevations = EuropaCleanElevations(
    none = 0.dp,
    low = 1.dp,
    medium = 3.dp,
    high = 6.dp,
)

internal val LocalEuropaCleanElevations = staticCompositionLocalOf<EuropaCleanElevations> {
    error("No EuropaCleanElevations provided")
}
