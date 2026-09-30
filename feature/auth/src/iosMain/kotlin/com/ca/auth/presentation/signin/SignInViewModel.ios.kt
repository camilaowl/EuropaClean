package com.ca.auth.presentation.signin

import androidx.lifecycle.ViewModel
import com.ca.auth.domain.AuthRepository

actual class SignInViewModel actual constructor(
    private val repository: AuthRepository
) : ViewModel()