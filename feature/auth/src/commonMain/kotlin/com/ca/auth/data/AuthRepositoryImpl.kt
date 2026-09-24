package com.ca.auth.data

import com.ca.auth.domain.AuthRepository

class AuthRepositoryImpl(
    private val authClient: AuthClient,
) : AuthRepository {

    override suspend fun isUserLoggedIn(): Boolean {
        return false
    }
}
