package com.example.altu.api

class ServerConfig(
    val baseUrl: String = DEFAULT_BASE_URL,
) {
    companion object {
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8081"
        const val INBOX_POLL_MS = 5_000L
    }
}
