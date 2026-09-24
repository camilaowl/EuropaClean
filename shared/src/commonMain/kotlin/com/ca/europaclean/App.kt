package com.ca.europaclean

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ca.europaclean.core.designsystem.theme.EuropaCleanTheme
import com.ca.europaclean.core.designsystem.theme.Theme
import com.ca.europaclean.navigation.AppNavHost

@Composable
fun App() {
    EuropaCleanTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Theme.colors.background,
            contentColor = Theme.colors.onBackground,
        ) {
            AppNavHost()
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
