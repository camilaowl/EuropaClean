package com.ca.auth.presentation.signin

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.ca.auth.domain.AuthRepository
import com.ca.authentication.GoogleSignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class SignInViewModel actual constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun handleGoogleSignInResult(result: GoogleSignInResult) {
        when (result) {
            is GoogleSignInResult.Success -> {
                // Handle successful sign-in
                Logger.d(tag = "Google ID", message = { "Google Sign-In successful: ${result.idToken}" })
                _uiState.value =
                    SignInUiState(isLoading = false, statusMessage = "Sign-in successful")
            }

            is GoogleSignInResult.Failed -> {
                _uiState.value = SignInUiState(
                    isLoading = false,
                    statusMessage = "Sign-in failed"
                )
            }

            else -> {}
        }
    }
}