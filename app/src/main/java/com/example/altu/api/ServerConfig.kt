package com.example.altu.api

import java.net.URI

class ServerConfig(
    val baseUrl: String = DEFAULT_BASE_URL,
) {
    companion object {
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8081"
        const val INBOX_POLL_MS = 5_000L

        fun normalize(raw: String): String? {
            val trimmed = raw.trim().trimEnd('/')
            if (trimmed.isBlank()) return null
            return try {
                val uri = URI(trimmed)
                val scheme = uri.scheme?.lowercase()
                if (scheme != "http" && scheme != "https") return null
                if (uri.host.isNullOrBlank()) return null
                trimmed
            } catch (_: Exception) {
                null
            }
        }
    }
}
