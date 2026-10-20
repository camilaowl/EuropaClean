package com.ca.authentication

interface TokenStorage {
    suspend fun accessToken(): String?
    suspend fun saveAccessToken(accessToken: String)
    suspend fun clear()
}
