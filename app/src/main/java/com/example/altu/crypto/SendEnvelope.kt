package com.example.altu.crypto

import java.util.Base64

class SendEnvelope(
    val rawBody: String,
    val timestampMillis: Long,
    val signature: ByteArray,
) {
    val timestampHeader: String get() = timestampMillis.toString()
    val signatureHeader: String get() = Base64.getEncoder().encodeToString(signature)

    companion object {
        const val TIMESTAMP_HEADER = "X-Timestamp"
        const val SIGNATURE_HEADER = "X-Signature"

        fun create(
            identity: Ed25519Identity,
            fromUserId: String,
            toUserId: String,
            chatId: String,
            sealed: SealedMessage,
            timestampMillis: Long = System.currentTimeMillis(),
        ): SendEnvelope {
            val encoder = Base64.getEncoder()
            val rawBody = buildString {
                append('{')
                append("\"fromUserId\":").append(jsonString(fromUserId)).append(',')
                append("\"toUserId\":").append(jsonString(toUserId)).append(',')
                append("\"chatId\":").append(jsonString(chatId)).append(',')
                append("\"nonce\":").append(jsonString(encoder.encodeToString(sealed.nonce))).append(',')
                append("\"ciphertext\":").append(jsonString(encoder.encodeToString(sealed.ciphertext)))
                append('}')
            }
            val payload = Ed25519Identity.signedPayload(timestampMillis, rawBody)
            return SendEnvelope(
                rawBody = rawBody,
                timestampMillis = timestampMillis,
                signature = identity.sign(payload),
            )
        }

        private fun jsonString(value: String): String {
            val escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
            return "\"$escaped\""
        }
    }
}
