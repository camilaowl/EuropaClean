package com.ca.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json

private val sensitiveHeaders = setOf(
    "authorization",
    "cookie",
    "proxy-authorization",
    "set-cookie",
)

internal fun createHttpClient(config: NetworkConfig): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
    install(HttpTimeout) {
        requestTimeoutMillis = config.requestTimeoutMillis
        connectTimeoutMillis = config.connectTimeoutMillis
        socketTimeoutMillis = config.socketTimeoutMillis
    }
    defaultRequest {
        config.normalizedBaseUrl?.let(::url)
        if (headers[HttpHeaders.Accept] == null) {
            headers.append(
                name = HttpHeaders.Accept,
                value = ContentType.Application.Json.toString(),
            )
        }
    }


    if (config.isLoggingEnabled) {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.HEADERS
            sanitizeHeader { header -> header.lowercase() in sensitiveHeaders }
        }
    }
}
