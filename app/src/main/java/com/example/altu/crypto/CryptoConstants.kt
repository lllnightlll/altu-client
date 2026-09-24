package com.example.altu.crypto

object CryptoConstants {
    const val ED25519_PUBLIC_KEY_SIZE = 32
    const val ED25519_SECRET_SEED_SIZE = 32
    const val ED25519_SIGNATURE_SIZE = 64
    const val X25519_KEY_SIZE = 32
    const val MESSAGE_NONCE_SIZE = 12
    const val POLY1305_TAG_SIZE = 16
    const val HKDF_KEY_SIZE = 32
    const val KEYSTORE_GCM_IV_SIZE = 12
    const val MESSAGE_INFO = "altu-message-v1"
}
