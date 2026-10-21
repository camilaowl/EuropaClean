package com.ca.authentication

class AuthSessionRepository(
    private val tokenStorage: TokenStorage
) {
    suspend fun isUserLoggedIn(): Boolean {
        return tokenStorage.accessToken() != null
    }
}