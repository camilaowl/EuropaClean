package com.ca.europaclean.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ca.auth.navigation.AuthNavGraph
import com.ca.europaclean.core.designsystem.theme.EuropaCleanTheme
import com.ca.europaclean.core.designsystem.theme.Theme
import com.ca.europaclean.navigation.AppNavHost
import com.ca.europaclean.navigation.MainGraph
import com.ca.navigation.TopLevelDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {

    val viewModel = koinViewModel<MainViewModel>()

    var startDestination: TopLevelDestination = MainGraph.Schedule

    LaunchedEffect(Unit) {
        val isLoggedIn = viewModel.isUserLoggedIn()
        startDestination = if (isLoggedIn) {
            MainGraph.Schedule
        } else {
            AuthNavGraph.SignIn
        }
    }

    EuropaCleanTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Theme.colors.background,
            contentColor = Theme.colors.onBackground,
        ) {
            AppNavHost(
                startDestination = startDestination
            )
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
