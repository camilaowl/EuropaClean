package com.ca.authentication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.MutableContextWrapper
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import java.security.SecureRandom
import java.util.Base64

class GoogleAuth(
    private val webClientId: String,
    private val credentialManager: CredentialManager,
) {
    suspend fun autoSignIn(activity: Activity): GoogleSignInResult {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption())
            .build()

        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = MutableContextWrapper(activity),
            )
            return parseCredential(result)
        } catch (_: NoCredentialException) {
            GoogleSignInResult.NoCredential
        } catch (_: GetCredentialCancellationException) {
            GoogleSignInResult.Cancelled
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Error during auto sign-in: ${e.message}", e)
            GoogleSignInResult.Failed
        }
    }

    suspend fun signIn(activity: Activity): GoogleSignInResult {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleSignInOption())
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = MutableContextWrapper(activity)
            )
            return parseCredential(result)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Error during Google sign-in: ${e.message}", e)
            return GoogleSignInResult.Failed
        }
    }

    private fun parseCredential(result: GetCredentialResponse?): GoogleSignInResult {
        val credential = result?.credential
        if (
            credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleSignInResult.Failed
        }

        val googleCredential = try {
            GoogleIdTokenCredential.createFrom(credential.data)
        } catch (_: GoogleIdTokenParsingException) {
            return GoogleSignInResult.Failed
        }

        return GoogleSignInResult.Success(
            idToken = googleCredential.idToken
        )
    }

    private fun googleSignInOption(): GetSignInWithGoogleOption {
        return GetSignInWithGoogleOption.Builder(webClientId)
            .setNonce(generateSecureRandomNonce())
            .build()
    }

    private fun googleIdOption(): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(true)
            .setNonce(generateSecureRandomNonce())
            .build()
    }
}

private fun generateSecureRandomNonce(byteLength: Int = 32): String {
    val randomBytes = ByteArray(byteLength)
    SecureRandom().nextBytes(randomBytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
}