package com.ca.auth.presentation.signin

data class SignInUiState(
    val isLoading: Boolean = false,
    val statusMessage: String? = null,
)
