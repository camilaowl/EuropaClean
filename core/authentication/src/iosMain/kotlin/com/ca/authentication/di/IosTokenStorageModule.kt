package com.ca.authentication.di

import com.ca.authentication.TokenStorage
import org.koin.dsl.module

fun iosTokenStorageModule() = module {
    single<TokenStorage> { DisabledTokenStorage }
}

private object DisabledTokenStorage : TokenStorage {
    override suspend fun accessToken(): String? = null

    override suspend fun saveAccessToken(accessToken: String) {
        error("Authentication is not supported on iOS yet.")
    }

    override suspend fun clear() = Unit
}
