package com.example.altu.api

import com.example.altu.crypto.Ed25519Identity
import com.example.altu.crypto.SendEnvelope
import java.util.Base64

class SignedHeaders(
    val timestampMillis: Long,
    val signatureBase64: String,
) {
    companion object {
        fun ofBody(identity: Ed25519Identity, rawBody: String, timestampMillis: Long = System.currentTimeMillis()): SignedHeaders {
            val payload = Ed25519Identity.signedPayload(timestampMillis, rawBody)
            return SignedHeaders(
                timestampMillis = timestampMillis,
                signatureBase64 = Base64.getEncoder().encodeToString(identity.sign(payload)),
            )
        }

        fun fromEnvelope(envelope: SendEnvelope): SignedHeaders {
            return SignedHeaders(
                timestampMillis = envelope.timestampMillis,
                signatureBase64 = envelope.signatureHeader,
            )
        }
    }
}
