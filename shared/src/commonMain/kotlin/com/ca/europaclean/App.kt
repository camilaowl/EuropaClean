package com.ca.europaclean

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ca.europaclean.core.designsystem.theme.EuropaCleanTheme
import com.ca.europaclean.core.designsystem.theme.Theme
import com.ca.europaclean.di.appModules
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.dsl.koinConfiguration
import org.jetbrains.compose.resources.painterResource

import europaclean.shared.generated.resources.Res
import europaclean.shared.generated.resources.compose_multiplatform

@Composable
fun App() {
    KoinApplication(
        configuration = koinConfiguration {
            modules(appModules())
        },
    ) {
        EuropaCleanTheme {
            AppContent()
        }
    }
}

@Composable
private fun AppContent(
    greeting: Greeting = koinInject(),
) {
    var showContent by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Theme.colors.primaryContainer,
        tonalElevation = Theme.elevations.none,
    ) {
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
        ) {
            Button(
                onClick = { showContent = !showContent },
                shape = Theme.shapes.medium,
            ) {
                Text(
                    text = "Click me!",
                    style = Theme.typography.labelLarge,
                )
            }
            AnimatedVisibility(showContent) {
                val greetingText = remember(greeting) { greeting.greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text(
                        text = "Compose: $greetingText",
                        style = Theme.typography.bodyLarge,
                        color = Theme.colors.onPrimaryContainer,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
