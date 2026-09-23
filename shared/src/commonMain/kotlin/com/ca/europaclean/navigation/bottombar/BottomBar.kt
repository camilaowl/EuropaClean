package com.ca.europaclean.navigation.bottombar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ca.europaclean.core.designsystem.theme.Theme
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        tonalElevation = BottomAppBarDefaults.ContainerElevation
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        MainDestination.bottomBarItems.forEach { item ->
            BottomBarItem(
                item = item,
                currentDestination = currentDestination,
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }
    }
}

@Composable
private fun RowScope.BottomBarItem(
    item: MainDestination,
    currentDestination: NavDestination?,
    onClick: () -> Unit
) {
    NavigationBarItem(
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Theme.colors.primary,
            unselectedIconColor = Theme.colors.onSurfaceVariant
        ),
        icon = { Icon(painter = painterResource(resource = item.icon), contentDescription = null) },
        selected = currentDestination?.hierarchy?.any { destination ->
            destination.hasRoute(item.route::class)
        } == true,
        onClick = onClick
    )
}
