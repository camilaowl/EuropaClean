package com.ca.auth.data

import com.ca.authentication.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking

class AuthRepositoryImplTest {
    @Test
    fun successfulSignInStoresBackendAccessToken() = runBlocking {
        val storage = RecordingTokenStorage()
        val client = responseClient(HttpStatusCode.OK, "Bearer backend-access-token")
        try {
            val repository = AuthRepositoryImpl(AuthClient(client), storage)

            assertTrue(repository.loginWithGoogle("google-id-token"))
            assertEquals("backend-access-token", storage.accessToken())
        } finally {
            client.close()
        }
    }

    @Test
    fun successfulResponseWithoutBearerTokenDoesNotSignIn() = runBlocking {
        for (header in listOf(null, "Basic other-token", "Bearer ")) {
            val storage = RecordingTokenStorage()
            val client = responseClient(HttpStatusCode.OK, header)
            try {
                val repository = AuthRepositoryImpl(AuthClient(client), storage)

                assertFalse(repository.loginWithGoogle("google-id-token"))
                assertNull(storage.accessToken())
            } finally {
                client.close()
            }
        }
    }

    @Test
    fun rejectedResponseDoesNotStoreToken() = runBlocking {
        val storage = RecordingTokenStorage()
        val client = responseClient(HttpStatusCode.Unauthorized, "Bearer backend-access-token")
        try {
            val repository = AuthRepositoryImpl(AuthClient(client), storage)

            assertFalse(repository.loginWithGoogle("google-id-token"))
            assertNull(storage.accessToken())
        } finally {
            client.close()
        }
    }

    @Test
    fun failedStorageDoesNotReportSuccessfulSignIn() = runBlocking {
        val storage = RecordingTokenStorage(failOnSave = true)
        val client = responseClient(HttpStatusCode.OK, "Bearer backend-access-token")
        try {
            val repository = AuthRepositoryImpl(AuthClient(client), storage)

            assertFailsWith<IllegalStateException> {
                repository.loginWithGoogle("google-id-token")
            }
            assertNull(storage.accessToken())
        } finally {
            client.close()
        }
    }

    private fun responseClient(status: HttpStatusCode, authorization: String?): HttpClient =
        HttpClient(MockEngine {
            respond(
                content = ByteReadChannel("{}"),
                status = status,
                headers = authorization?.let { headersOf(HttpHeaders.Authorization, it) }
                    ?: Headers.Empty,
            )
        }) {
            install(ContentNegotiation) { json() }
        }

    private class RecordingTokenStorage(
        private val failOnSave: Boolean = false,
    ) : TokenStorage {
        private var token: String? = null

        override suspend fun accessToken(): String? = token

        override suspend fun saveAccessToken(accessToken: String) {
            if (failOnSave) throw IllegalStateException("Storage unavailable")
            token = accessToken
        }

        override suspend fun clear() {
            token = null
        }
    }
}
