package com.example.altu.crypto

import org.bouncycastle.crypto.agreement.X25519Agreement
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.generators.HKDFBytesGenerator
import org.bouncycastle.crypto.modes.ChaCha20Poly1305
import org.bouncycastle.crypto.params.AEADParameters
import org.bouncycastle.crypto.params.HKDFParameters
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters
import org.bouncycastle.crypto.params.X25519PublicKeyParameters
import java.security.SecureRandom

data class SealedMessage(
    val nonce: ByteArray,
    val ciphertext: ByteArray,
)

class MessageCipher(
    private val identity: Ed25519Identity,
) {
    private val random = SecureRandom()

    fun encrypt(
        plaintext: String,
        recipientEd25519Public: ByteArray,
        associatedData: ByteArray,
    ): SealedMessage {
        val nonce = ByteArray(CryptoConstants.MESSAGE_NONCE_SIZE).also { random.nextBytes(it) }
        val key = sharedKey(recipientEd25519Public)
        val cipher = ChaCha20Poly1305()
        cipher.init(true, AEADParameters(KeyParameter(key), CryptoConstants.POLY1305_TAG_SIZE * 8, nonce, associatedData))
        val input = plaintext.toByteArray(Charsets.UTF_8)
        val output = ByteArray(cipher.getOutputSize(input.size))
        val written = cipher.processBytes(input, 0, input.size, output, 0)
        cipher.doFinal(output, written)
        return SealedMessage(nonce = nonce, ciphertext = output)
    }

    fun decrypt(
        nonce: ByteArray,
        ciphertext: ByteArray,
        otherEd25519Public: ByteArray,
        associatedData: ByteArray,
    ): String? {
        return try {
            val key = sharedKey(otherEd25519Public)
            val cipher = ChaCha20Poly1305()
            cipher.init(false, AEADParameters(KeyParameter(key), CryptoConstants.POLY1305_TAG_SIZE * 8, nonce, associatedData))
            val output = ByteArray(cipher.getOutputSize(ciphertext.size))
            val written = cipher.processBytes(ciphertext, 0, ciphertext.size, output, 0)
            val finalWritten = written + cipher.doFinal(output, written)
            String(output, 0, finalWritten, Charsets.UTF_8)
        } catch (_: Exception) {
            null
        }
    }

    private fun sharedKey(otherEd25519Public: ByteArray): ByteArray {
        val agreement = X25519Agreement()
        agreement.init(X25519PrivateKeyParameters(identity.x25519PrivateKey(), 0))
        val shared = ByteArray(agreement.agreementSize)
        agreement.calculateAgreement(
            X25519PublicKeyParameters(Curve25519Convert.publicKey(otherEd25519Public), 0),
            shared,
            0,
        )
        val hkdf = HKDFBytesGenerator(SHA256Digest())
        hkdf.init(HKDFParameters(shared, null, CryptoConstants.MESSAGE_INFO.toByteArray(Charsets.UTF_8)))
        val key = ByteArray(CryptoConstants.HKDF_KEY_SIZE)
        hkdf.generateBytes(key, 0, key.size)
        return key
    }

    companion object {
        fun associatedData(fromUserId: String, toUserId: String, chatId: String): ByteArray {
            return "$fromUserId|$toUserId|$chatId".toByteArray(Charsets.UTF_8)
        }
    }
}
