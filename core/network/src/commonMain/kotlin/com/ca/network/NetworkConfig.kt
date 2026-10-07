package com.ca.network

data class NetworkConfig(
    val baseUrl: String? = null,
    val requestTimeoutMillis: Long = 30_000,
    val connectTimeoutMillis: Long = 15_000,
    val socketTimeoutMillis: Long = 30_000,
    val isLoggingEnabled: Boolean = false,
) {
    init {
        require(baseUrl == null || baseUrl.isNotBlank()) {
            "baseUrl must be null or non-blank"
        }
        require(requestTimeoutMillis > 0) {
            "requestTimeoutMillis must be greater than zero"
        }
        require(connectTimeoutMillis > 0) {
            "connectTimeoutMillis must be greater than zero"
        }
        require(socketTimeoutMillis > 0) {
            "socketTimeoutMillis must be greater than zero"
        }
    }

    internal val normalizedBaseUrl: String?
        get() = baseUrl?.trimEnd('/')?.plus('/')
}
