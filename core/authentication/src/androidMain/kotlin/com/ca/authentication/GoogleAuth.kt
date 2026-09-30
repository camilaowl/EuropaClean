package com.ca.authentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.MutableContextWrapper
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.coroutineScope
import java.security.SecureRandom

class GoogleAuth(
    private val webClientId: String,
    private val credentialManager: CredentialManager
) {
    suspend fun autoSignIn(activity: Activity) {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption(webClientId))
            .build()

        coroutineScope {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = MutableContextWrapper(activity),
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                // Handle failures
            }
        }
    }

    suspend fun signIn(activity: Activity) {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleSignInOption(webClientId))
            .build()

        coroutineScope {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = MutableContextWrapper(activity),
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                // Handle failures
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when(result.credential) {
            is CustomCredential -> {
                if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID for server-side validation.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(result.credential.data)
                        Log.d(TAG, "ID TOKEN: ${googleIdTokenCredential.idToken} ", )
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun googleSignInOption(webClientId: String): GetSignInWithGoogleOption {
        return GetSignInWithGoogleOption.Builder(webClientId)
            .setNonce(generateSecureRandomNonce())
            .build()
    }

    private fun googleIdOption(webClientId: String): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .setNonce(generateSecureRandomNonce())
            .build()
    }

    private fun generateSecureRandomNonce(byteLength: Int = 32): String {
        val randomBytes = ByteArray(byteLength)
        SecureRandom().nextBytes(randomBytes)
        return android.util.Base64.encodeToString(randomBytes, android.util.Base64.NO_WRAP or android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING)
    }
}
