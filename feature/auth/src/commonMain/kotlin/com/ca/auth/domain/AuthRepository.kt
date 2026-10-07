package com.ca.auth.domain

interface AuthRepository {
    suspend fun isUserLoggedIn(): Boolean
    suspend fun loginWithGoogle(idToken: String): Boolean
}
