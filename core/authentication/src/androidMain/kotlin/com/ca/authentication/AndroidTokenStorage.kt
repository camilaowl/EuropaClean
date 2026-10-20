package com.ca.authentication

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import java.security.KeyStore
import java.util.Base64
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class AndroidTokenStorage(
    private val dataStore: DataStore<Preferences>,
    keyStoreFactory: () -> KeyStore = {
        KeyStore.getInstance(KEY_STORE_PROVIDER).apply { load(null) }
    },
) : TokenStorage {
    private val mutex = Mutex()
    private val tokenKey = stringPreferencesKey("encrypted_access_token")

    private val keyStore: KeyStore by lazy(keyStoreFactory)

    override suspend fun accessToken(): String? = withContext(Dispatchers.IO) {
        mutex.withLock {
            val encryptedToken = dataStore.data.first()[tokenKey] ?: return@withLock null
            val token = try {
                decrypt(encryptedToken)
            } catch (_: AEADBadTagException) {
                null
            }

            if (token == null) {
                // A lost key or damaged ciphertext requires a new sign-in.
                clearStoredToken()
            }
            token
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        require(accessToken.isNotBlank()) { "Access token must not be blank." }

        withContext(Dispatchers.IO) {
            mutex.withLock {
                val cipher = Cipher.getInstance(TRANSFORMATION)
                // Let Keystore generate a fresh random IV for every encryption.
                cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
                val ciphertext = cipher.doFinal(accessToken.toByteArray(Charsets.UTF_8))
                val encryptedToken = Base64.getEncoder().encodeToString(cipher.iv + ciphertext)

                dataStore.edit { preferences ->
                    preferences[tokenKey] = encryptedToken
                }
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            mutex.withLock { clearStoredToken() }
        }
    }

    private fun decrypt(encryptedToken: String): String? {
        val payload = try {
            Base64.getDecoder().decode(encryptedToken)
        } catch (_: IllegalArgumentException) {
            return null
        }
        if (payload.size <= IV_SIZE_BYTES + TAG_SIZE_BITS / Byte.SIZE_BITS) return null

        val key = keyStore.getKey(KEY_ALIAS, null) as? SecretKey ?: return null
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            key,
            GCMParameterSpec(TAG_SIZE_BITS, payload, 0, IV_SIZE_BYTES),
        )
        return cipher.doFinal(payload, IV_SIZE_BYTES, payload.size - IV_SIZE_BYTES)
            .toString(Charsets.UTF_8)
            .takeIf { it.isNotBlank() }
    }

    private fun getOrCreateKey(): SecretKey {
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_STORE_PROVIDER).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build(),
            )
        }.generateKey()
    }

    private suspend fun clearStoredToken() {
        // Keep the reusable encryption key so clearing a token does not depend on Keystore.
        dataStore.edit { preferences -> preferences.remove(tokenKey) }
    }

    private companion object {
        const val KEY_STORE_PROVIDER = "AndroidKeyStore"
        const val KEY_ALIAS = "europa_clean_access_token"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val IV_SIZE_BYTES = 12
        const val TAG_SIZE_BITS = 128
    }
}
