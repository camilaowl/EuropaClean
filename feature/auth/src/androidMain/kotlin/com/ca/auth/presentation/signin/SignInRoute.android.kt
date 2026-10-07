package com.ca.auth.presentation.signin

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ca.authentication.GoogleAuth
import com.ca.europaclean.core.designsystem.theme.Theme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun SignInRoute(
    onSignIn: () -> Unit,
) {
    val activity = LocalActivity.current
    val googleAuth = koinInject<GoogleAuth>()
    val viewModel = koinViewModel<SignInViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        activity?.let {
            val result = googleAuth.autoSignIn(it)
            viewModel.handleGoogleSignInResult(result)
        }
    }

    LaunchedEffect(uiState.isSignedIn) {
        if (uiState.isSignedIn) {
            onSignIn()
        }
    }

    SignInContent(
        uiState = uiState,
        onGoogleSignInClick = {
            coroutineScope.launch {
                activity?.let {
                    val result = googleAuth.signIn(it)
                    viewModel.handleGoogleSignInResult(result)
                }
            }
        }
    )
}

@Composable
internal fun SignInContent(
    uiState: SignInUiState,
    onGoogleSignInClick: () -> Unit,
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
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Theme.colors.primary,
                    contentColor = Theme.colors.onPrimary,
                ),
            ) {
                Text(text = "Continue with Google")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            uiState.statusMessage?.let { message ->
                Text(
                    text = message,
                    color = Theme.colors.onBackground,
                    style = Theme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
