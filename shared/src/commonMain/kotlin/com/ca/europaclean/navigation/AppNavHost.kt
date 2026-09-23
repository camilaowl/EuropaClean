package com.ca.europaclean.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ca.auth.navigation.AuthNavGraph
import com.ca.auth.navigation.authNavGraph
import com.ca.europaclean.navigation.bottombar.BottomBarNavHost
import com.ca.navigation.TopLevelDestination

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    startGraph: TopLevelDestination = AuthNavGraph,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startGraph,
        modifier = modifier,
    ) {
        authNavGraph(
            onSignIn = {
                navController.navigate(MainGraph.Schedule) {
                    popUpTo(AuthNavGraph) { inclusive = true }
                }
            }
        )

        composable<MainGraph.Schedule> {
            BottomBarNavHost(
                modifier = modifier
            )
        }
    }
}
