package com.ca.europaclean.navigation.bottombar

import com.ca.europaclean.core.designsystem.Resources
import org.jetbrains.compose.resources.DrawableResource

internal sealed class MainDestination (val route: BottomBarRoute, val icon: DrawableResource) {
    data object Schedule : MainDestination(BottomBarRoute.Schedule, Resources.Icon.Schedule)
    data object MyHome : MainDestination(BottomBarRoute.MyHome, Resources.Icon.Home)
    data object Settings : MainDestination(BottomBarRoute.Settings, Resources.Icon.Settings)

    companion object {
        val bottomBarItems = listOf(
            Schedule,
            MyHome,
            Settings
        )
    }
}
