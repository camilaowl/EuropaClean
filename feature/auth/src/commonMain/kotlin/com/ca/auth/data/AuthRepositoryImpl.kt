package com.ca.auth.data

import com.ca.auth.domain.AuthRepository

internal class AuthRepositoryImpl(
    private val authClient: AuthClient
) : AuthRepository {

    override suspend fun loginWithGoogle(idToken: String): Boolean {
        return authClient.loginWithGoogle(idToken).status.value in 200..299
    }
}
