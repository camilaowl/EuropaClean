package com.ca.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import java.security.KeyStoreException
import java.security.UnrecoverableKeyException
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AndroidTokenStorageFailureTest {
    private val tokenKey = stringPreferencesKey("encrypted_access_token")
    // A structurally valid payload ensures reads reach Keystore before decryption.
    private val encodedPayload = Base64.getEncoder().encodeToString(ByteArray(32))
    private val dataStore = TestDataStore()

    @Test
    fun keyStoreFailurePreservesStoredToken() = runBlocking {
        dataStore.edit { it[tokenKey] = encodedPayload }
        val failure = UnrecoverableKeyException("Keystore temporarily unavailable")
        val storage = AndroidTokenStorage(dataStore) { throw failure }

        val thrown = assertFailsWith<UnrecoverableKeyException> { storage.accessToken() }
        assertEquals(failure.message, thrown.message)
        assertEquals(encodedPayload, dataStore.data.first()[tokenKey])
    }

    @Test
    fun keyStoreArgumentFailureIsNotTreatedAsMalformedBase64() = runBlocking {
        dataStore.edit { it[tokenKey] = encodedPayload }
        val failure = IllegalArgumentException("Provider failure")
        val storage = AndroidTokenStorage(dataStore) { throw failure }

        val thrown = assertFailsWith<IllegalArgumentException> { storage.accessToken() }
        assertEquals(failure.message, thrown.message)
        assertEquals(encodedPayload, dataStore.data.first()[tokenKey])
    }

    @Test
    fun clearSucceedsWithoutAccessingKeyStore() = runBlocking {
        dataStore.edit { it[tokenKey] = encodedPayload }
        val storage = AndroidTokenStorage(dataStore) {
            throw KeyStoreException("Keystore unavailable")
        }

        storage.clear()

        assertNull(dataStore.data.first()[tokenKey])
        assertNull(storage.accessToken())
    }

    @Test
    fun malformedAndTruncatedPayloadsAreClearedWithoutAccessingKeyStore() = runBlocking {
        val storage = AndroidTokenStorage(dataStore) {
            error("Invalid payloads must be handled without Keystore")
        }
        for (payload in listOf("!invalid-base64!", "AA==")) {
            dataStore.edit { it[tokenKey] = payload }

            assertNull(storage.accessToken())
            assertNull(dataStore.data.first()[tokenKey])
        }
    }

    @Test
    fun persistenceFailureDuringClearIsPropagated() = runBlocking {
        dataStore.edit { it[tokenKey] = encodedPayload }
        val failure = IOException("Write failed")
        dataStore.writeFailure = failure
        val storage = AndroidTokenStorage(dataStore)

        val thrown = assertFailsWith<IOException> { storage.clear() }
        assertEquals(failure.message, thrown.message)
        assertEquals(encodedPayload, dataStore.data.first()[tokenKey])
    }

    @Test
    fun cancelledReadPreservesStoredToken() = runBlocking {
        dataStore.edit { it[tokenKey] = encodedPayload }
        val storage = AndroidTokenStorage(dataStore) { throw CancellationException("Cancelled") }

        assertFailsWith<CancellationException> { storage.accessToken() }
        assertEquals(encodedPayload, dataStore.data.first()[tokenKey])
    }

    private class TestDataStore : DataStore<Preferences> {
        private val preferences = MutableStateFlow(emptyPreferences())
        private val mutex = Mutex()
        var writeFailure: IOException? = null
        override val data: Flow<Preferences> = preferences

        override suspend fun updateData(
            transform: suspend (Preferences) -> Preferences,
        ): Preferences = mutex.withLock {
            writeFailure?.let { throw it }
            transform(preferences.value).also { preferences.value = it }
        }
    }
}
