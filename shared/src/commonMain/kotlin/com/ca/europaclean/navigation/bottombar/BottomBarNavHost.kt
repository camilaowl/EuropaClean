package com.ca.europaclean.navigation.bottombar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ca.europaclean.core.designsystem.theme.Theme
import com.ca.schedule.presentation.ScheduleRoute
import com.ca.settings.presentation.SettingsRoute
import com.ca.spaces.presentation.SpacesRoute

@Composable
fun BottomBarNavHost(
    modifier: Modifier = Modifier
) {
    val bottomBarNavHostController = rememberNavController()

    Scaffold(
        containerColor = Theme.colors.background,
        contentColor = Theme.colors.onBackground,
        bottomBar = { BottomBar(navController = bottomBarNavHostController) }
    ) { innerPadding ->
        NavHost(
            navController = bottomBarNavHostController,
            startDestination = BottomBarRoute.Schedule,
            modifier = modifier.padding(innerPadding)
        ) {
            composable<BottomBarRoute.Schedule> {
                ScheduleRoute()
            }

            composable<BottomBarRoute.MyHome> {
                SpacesRoute()
            }

            composable<BottomBarRoute.Settings> {
                SettingsRoute()
            }
        }

    }
}