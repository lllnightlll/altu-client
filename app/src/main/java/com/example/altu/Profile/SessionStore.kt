package com.example.altu.Profile

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionStore(
    context: Context,
) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _session = MutableStateFlow(read())
    val session: StateFlow<Session?> = _session.asStateFlow()

    fun isRegistered(): Boolean = _session.value != null

    fun userId(): String? = _session.value?.userId

    fun save(userId: String, tag: String, avatarPath: String?) {
        prefs.edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_TAG, tag)
            .putString(KEY_AVATAR_PATH, avatarPath)
            .apply()
        _session.value = Session(userId = userId, tag = tag, avatarPath = avatarPath)
    }

    fun clear() {
        prefs.edit().clear().apply()
        _session.value = null
    }

    private fun read(): Session? {
        val userId = prefs.getString(KEY_USER_ID, null) ?: return null
        val tag = prefs.getString(KEY_TAG, null) ?: return null
        if (userId.isBlank() || tag.isBlank()) return null
        return Session(
            userId = userId,
            tag = tag,
            avatarPath = prefs.getString(KEY_AVATAR_PATH, null),
        )
    }

    companion object {
        private const val PREFS_NAME = "altu_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_TAG = "tag"
        private const val KEY_AVATAR_PATH = "avatar_path"
    }
}
