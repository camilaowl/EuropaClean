package com.ca.authentication

import android.content.Context
import java.security.KeyStore



internal class AndroidTokenStorage(
    private val context: Context
) : TokenStorage {

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }
    }

    override suspend fun accessToken(): String? {
        // Implement the logic to retrieve the access token from Android's secure storage
        return null
    }

    override suspend fun refreshToken(): String? {
        // Implement the logic to retrieve the refresh token from Android's secure storage
        return null
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        // Implement the logic to save the access and refresh tokens to Android's secure storage
    }

    override suspend fun clear() {
        // Implement the logic to clear the tokens from Android's secure storage
    }

}