package com.ca.auth.data

import com.ca.auth.domain.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class AuthRepositoryImpl(
    private val authClient: AuthClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {

    override suspend fun isUserLoggedIn(): Boolean {
        return false
    }
}
