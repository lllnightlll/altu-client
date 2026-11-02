package com.example.altu.api

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ServerUrlStore(
    context: Context,
) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _url = MutableStateFlow(read())
    val url: StateFlow<String> = _url.asStateFlow()

    fun save(raw: String): Boolean {
        val normalized = ServerConfig.normalize(raw) ?: return false
        prefs.edit().putString(KEY_BASE_URL, normalized).apply()
        _url.value = normalized
        return true
    }

    private fun read(): String {
        return ServerConfig.normalize(prefs.getString(KEY_BASE_URL, null).orEmpty())
            ?: ServerConfig.DEFAULT_BASE_URL
    }

    companion object {
        private const val PREFS_NAME = "altu_server"
        private const val KEY_BASE_URL = "base_url"
    }
}
