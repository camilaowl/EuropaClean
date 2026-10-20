package com.ca.auth.data

import com.ca.auth.domain.AuthRepository
import com.ca.authentication.TokenStorage
import io.ktor.http.HttpHeaders

internal class AuthRepositoryImpl(
    private val authClient: AuthClient,
    private val tokenStorage: TokenStorage,
) : AuthRepository {

    override suspend fun loginWithGoogle(idToken: String): Boolean {
        val response = authClient.loginWithGoogle(idToken)
        if (response.status.value !in 200..299) return false

        val accessToken = response.headers[HttpHeaders.Authorization]
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.takeIf { it.isNotBlank() && it.none(Char::isWhitespace) }
            ?: return false

        tokenStorage.saveAccessToken(accessToken)
        return true
    }
}
