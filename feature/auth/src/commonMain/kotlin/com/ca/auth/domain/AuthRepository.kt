package com.ca.auth.domain

interface AuthRepository {
    suspend fun loginWithGoogle(idToken: String): Boolean
}
