package com.ca.authentication

import android.content.ContentValues.TAG
import android.content.Context
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

actual class GoogleAuth(
    private val context: Any,
    private val credentialManager: CredentialManager,
    private val googleIdOption: GetGoogleIdOption,
    private val googleSignInOption: GetSignInWithGoogleOption
) {
    actual suspend fun googleIdToken(): GoogleAuthCredential {
        // Implement the logic to retrieve the Google ID token on Android
        // This is a placeholder implementation and should be replaced with actual code
        return GoogleAuthCredential(idToken = "dummy_id_token", accessToken = "dummy_access_token")
    }

    actual suspend fun autoSignIn() {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context as Context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                // Handle failures
            }
        }
    }

    actual suspend fun signIn() {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleSignInOption)
            .build()

        coroutineScope {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context as Context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                // Handle failures
            }
        }
    }

    fun handleSignIn(result: GetCredentialResponse) {
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
}