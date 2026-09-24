package com.ca.auth.presentation.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ca.europaclean.core.designsystem.theme.Theme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInRoute(
    onSignIn: () -> Unit,
) {
    val viewModel = koinViewModel<SignInViewModel>()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.autoSignIn()
        }
    }

    SignInScreen(
        onGoogleSignInClick = {
            scope.launch {
                viewModel.signInWithGoogle()
            }
        }
    )
}

@Composable
internal fun SignInScreen(
    onGoogleSignInClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Theme.colors.background,
        contentColor = Theme.colors.onBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = Theme.spacing.medium,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            Text(
                text = "Sign In",
                color = Theme.colors.onBackground,
                style = Theme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = onGoogleSignInClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Theme.colors.primary,
                    contentColor = Theme.colors.onPrimary,
                ),
            ) {
                Text(
                    text = "Sign In",
                    style = Theme.typography.labelLarge,
                )
            }
        }
    }
}