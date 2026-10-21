package com.ca.auth.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.ca.auth.domain.AuthRepository
import com.ca.authentication.GoogleSignInResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

actual class SignInViewModel actual constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun handleGoogleSignInResult(result: GoogleSignInResult) {
        when (result) {
            is GoogleSignInResult.Success -> {
                _uiState.value = SignInUiState(isLoading = true)
                viewModelScope.launch {
                    try {
                        val isSignedIn = repository.loginWithGoogle(result.idToken)
                        _uiState.value = if (isSignedIn) {
                            Logger.d(tag = "Sign-In", message = { "Sign-in successful" })
                            SignInUiState(
                                isSignedIn = true,
                                statusMessage = "Sign-in successful",
                            )
                        } else {
                            Logger.e(tag = "Sign-In", message = { "Sign-in rejected" })
                            SignInUiState(statusMessage = "Sign-in failed")
                        }
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (exception: Exception) {
                        Logger.e(
                            throwable = exception,
                            tag = "Sign-In",
                            message = { "Sign-in request failed" },
                        )
                        _uiState.value = SignInUiState(
                            statusMessage = "Unable to sign in. Please try again.",
                        )
                    }
                }
            }

            is GoogleSignInResult.Failed -> {
                _uiState.value = SignInUiState(
                    isLoading = false,
                    statusMessage = "Sign-in failed"
                )
            }

            GoogleSignInResult.Cancelled,
            GoogleSignInResult.NoCredential,
            -> Unit
        }
    }
}
