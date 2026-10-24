package com.example.altu.NewContact

import com.example.altu.crypto.CryptoConstants
import org.json.JSONObject
import java.util.Base64

data class ContactQr(
    val userId: String,
    val tag: String,
    val publicKey: ByteArray,
) {
    fun toPayload(): String {
        val key = Base64.getEncoder().encodeToString(publicKey)
        return JSONObject()
            .put(KEY_VERSION, VERSION)
            .put(KEY_USER_ID, userId)
            .put(KEY_TAG, tag)
            .put(KEY_PUBLIC_KEY, key)
            .toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ContactQr) return false
        return userId == other.userId &&
            tag == other.tag &&
            publicKey.contentEquals(other.publicKey)
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + publicKey.contentHashCode()
        return result
    }

    companion object {
        const val VERSION = 1
        private const val KEY_VERSION = "v"
        private const val KEY_USER_ID = "userId"
        private const val KEY_TAG = "tag"
        private const val KEY_PUBLIC_KEY = "publicKey"

        fun parse(raw: String): ContactQr? {
            return try {
                val json = JSONObject(raw)
                if (json.optInt(KEY_VERSION, 0) != VERSION) return null
                val userId = json.getString(KEY_USER_ID)
                val tag = json.getString(KEY_TAG)
                val key = Base64.getDecoder().decode(json.getString(KEY_PUBLIC_KEY))
                if (userId.isBlank() || tag.isBlank()) return null
                if (key.size != CryptoConstants.ED25519_PUBLIC_KEY_SIZE) return null
                ContactQr(userId = userId, tag = tag, publicKey = key)
            } catch (_: Exception) {
                null
            }
        }
    }
}
