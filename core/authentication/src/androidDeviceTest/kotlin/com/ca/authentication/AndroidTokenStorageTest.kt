package com.ca.authentication

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.security.KeyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidTokenStorageTest {
    @get:Rule
    val folder = TemporaryFolder(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir)

    private lateinit var storeJob: Job
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var storage: AndroidTokenStorage
    private val tokenKey = stringPreferencesKey("encrypted_access_token")

    @Before
    fun setUp() {
        openStorage()
    }

    @After
    fun tearDown() = runBlocking {
        try {
            storage.clear()
        } finally {
            storeJob.cancelAndJoin()
            KeyStore.getInstance("AndroidKeyStore").apply {
                load(null)
                deleteEntry("europa_clean_access_token")
            }
        }
    }

    @Test
    fun emptyStorageHasNoSession() = runBlocking {
        assertNull(storage.accessToken())
    }

    @Test
    fun tokenSurvivesReopeningWithoutPersistingPlaintext() = runBlocking {
        val token = "sample-backend-access-token"
        storage.saveAccessToken(token)

        val persisted = dataStore.data.first()[tokenKey]!!
        assertNotEquals(token, persisted)
        assertFalse(Base64.decode(persisted, Base64.NO_WRAP).toString(Charsets.UTF_8).contains(token))
        assertFalse(folder.root.resolve("tokens.preferences_pb").readText().contains(token))

        storeJob.cancelAndJoin()
        openStorage()

        assertEquals(token, storage.accessToken())
    }

    @Test
    fun savingTheSameTokenUsesDifferentCiphertext() = runBlocking {
        storage.saveAccessToken("sample-token")
        val first = dataStore.data.first()[tokenKey]

        storage.saveAccessToken("sample-token")
        val second = dataStore.data.first()[tokenKey]

        assertNotEquals(first, second)
        assertEquals("sample-token", storage.accessToken())
    }

    @Test
    fun clearingRemovesSessionAndAllowsSigningInAgain() = runBlocking {
        storage.saveAccessToken("old-token")
        storage.clear()

        assertNull(storage.accessToken())
        assertTrue(dataStore.data.first().asMap().isEmpty())

        storage.saveAccessToken("new-token")
        assertEquals("new-token", storage.accessToken())
    }

    @Test
    fun tamperedCiphertextClearsSessionAndAllowsSigningInAgain() = runBlocking {
        storage.saveAccessToken("sample-token")
        val payload = Base64.decode(dataStore.data.first()[tokenKey]!!, Base64.NO_WRAP)
        payload[payload.lastIndex] = (payload.last().toInt() xor 1).toByte()
        dataStore.edit { it[tokenKey] = Base64.encodeToString(payload, Base64.NO_WRAP) }

        assertNull(storage.accessToken())
        assertTrue(dataStore.data.first().asMap().isEmpty())

        storage.saveAccessToken("new-token")
        assertEquals("new-token", storage.accessToken())
    }

    @Test
    fun missingKeyClearsSessionAndAllowsSigningInAgain() = runBlocking {
        storage.saveAccessToken("sample-token")
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
            deleteEntry("europa_clean_access_token")
        }

        assertNull(storage.accessToken())
        assertTrue(dataStore.data.first().asMap().isEmpty())

        storage.saveAccessToken("new-token")
        assertEquals("new-token", storage.accessToken())
    }

    private fun openStorage() {
        storeJob = SupervisorJob()
        dataStore = DataStoreFactory.create(
            storage = FileStorage(
                serializer = PreferencesFileSerializer,
                produceFile = { folder.root.resolve("tokens.preferences_pb") },
            ),
            scope = CoroutineScope(Dispatchers.IO + storeJob),
        )
        storage = AndroidTokenStorage(dataStore)
    }
}
