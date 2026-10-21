package com.ca.auth.presentation.signin

import androidx.lifecycle.ViewModel
import com.ca.auth.domain.AuthRepository

expect class SignInViewModel(repository: AuthRepository) : ViewModel
