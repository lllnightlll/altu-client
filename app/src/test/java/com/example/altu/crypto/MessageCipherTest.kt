package com.example.altu.crypto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.security.SecureRandom

class MessageCipherTest {
    @Test
    fun encryptThenDecryptNotesToSelf() {
        val seed = ByteArray(CryptoConstants.ED25519_SECRET_SEED_SIZE)
        SecureRandom().nextBytes(seed)
        val identity = Ed25519Identity(seed)
        val cipher = MessageCipher(identity)
        val aad = MessageCipher.associatedData("me", "me", "me")
        val sealed = cipher.encrypt("hello notes", identity.publicKey, aad)
        val plain = cipher.decrypt(sealed.nonce, sealed.ciphertext, identity.publicKey, aad)
        assertEquals("hello notes", plain)
    }

    @Test
    fun sendEnvelopeSignatureMatchesServerPayload() {
        val seed = ByteArray(CryptoConstants.ED25519_SECRET_SEED_SIZE)
        SecureRandom().nextBytes(seed)
        val identity = Ed25519Identity(seed)
        val sealed = SealedMessage(nonce = ByteArray(12), ciphertext = byteArrayOf(1, 2, 3))
        val envelope = SendEnvelope.create(
            identity = identity,
            fromUserId = "from",
            toUserId = "to",
            chatId = "chat",
            sealed = sealed,
            timestampMillis = 1_700_000_000_000L,
        )
        val payload = Ed25519Identity.signedPayload(envelope.timestampMillis, envelope.rawBody)
        assertTrue(Ed25519Identity.verify(identity.publicKey, payload, envelope.signature))
        assertEquals(CryptoConstants.ED25519_SIGNATURE_SIZE, envelope.signature.size)
        assertEquals(CryptoConstants.ED25519_PUBLIC_KEY_SIZE, identity.publicKey.size)
    }
}
