package com.ca.auth.presentation.signin

import androidx.lifecycle.ViewModel
import com.ca.auth.domain.AuthRepository

class SignInViewModel(val repository: AuthRepository) : ViewModel() {

    suspend fun autoSignIn() = repository.tryAutoSignIn()
    suspend fun signInWithGoogle() = repository.signInWithGoogle()
}