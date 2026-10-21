package com.ca.auth.data

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class AuthClient(
    private val httpClient: HttpClient,
) {
    suspend fun loginWithGoogle(idToken: String): HttpResponse {
        return httpClient.post("api/auth/google") {
            contentType(ContentType.Application.Json)
            setBody(
                GoogleSignInRequest(
                    google = GoogleCredentials(idToken = idToken),
                ),
            )
        }
    }
}

@Serializable
private data class GoogleSignInRequest(
    val google: GoogleCredentials,
)

@Serializable
private data class GoogleCredentials(
    @SerialName("id_token")
    val idToken: String,
)
