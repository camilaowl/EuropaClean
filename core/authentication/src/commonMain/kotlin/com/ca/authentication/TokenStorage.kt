package com.ca.authentication

interface TokenStorage {
    suspend fun accessToken(): String?
    suspend fun refreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String?)
    suspend fun clear()
}