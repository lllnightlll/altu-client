package com.example.altu.crypto

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer

class Ed25519Identity(
    val seed: ByteArray,
) {
    private val privateKey = Ed25519PrivateKeyParameters(seed)
    private val publicKeyParams: Ed25519PublicKeyParameters = privateKey.generatePublicKey()

    val publicKey: ByteArray = publicKeyParams.encoded

    fun sign(message: ByteArray): ByteArray {
        val signer = Ed25519Signer()
        signer.init(true, privateKey)
        signer.update(message, 0, message.size)
        return signer.generateSignature()
    }

    fun x25519PrivateKey(): ByteArray = Curve25519Convert.privateKey(seed)

    fun x25519PublicKey(): ByteArray = Curve25519Convert.publicKey(publicKey)

    companion object {
        fun verify(publicKeyRaw: ByteArray, message: ByteArray, signature: ByteArray): Boolean {
            if (publicKeyRaw.size != CryptoConstants.ED25519_PUBLIC_KEY_SIZE) return false
            if (signature.size != CryptoConstants.ED25519_SIGNATURE_SIZE) return false
            return try {
                val verifier = Ed25519Signer()
                verifier.init(false, Ed25519PublicKeyParameters(publicKeyRaw))
                verifier.update(message, 0, message.size)
                verifier.verifySignature(signature)
            } catch (_: Exception) {
                false
            }
        }

        fun signedPayload(timestampMillis: Long, rawBody: String): ByteArray {
            return "$timestampMillis\n$rawBody".toByteArray(Charsets.UTF_8)
        }
    }
}
