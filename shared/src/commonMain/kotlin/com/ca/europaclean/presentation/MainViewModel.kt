package com.ca.europaclean.presentation

import androidx.lifecycle.ViewModel
import com.ca.authentication.AuthSessionRepository

class MainViewModel(private val sessionRepository: AuthSessionRepository): ViewModel() {
    suspend fun isUserLoggedIn(): Boolean {
        return sessionRepository.isUserLoggedIn()
    }
}