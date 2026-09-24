package com.example.altu.crypto

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class IdentityStore(
    context: Context,
) {
    private val appContext = context.applicationContext
    private val boxFile = File(appContext.filesDir, BOX_NAME)

    fun loadOrCreate(): Ed25519Identity {
        val seed = if (boxFile.exists()) {
            unwrap(boxFile.readBytes())
        } else {
            val created = ByteArray(CryptoConstants.ED25519_SECRET_SEED_SIZE)
            SecureRandom().nextBytes(created)
            boxFile.writeBytes(wrap(created))
            created
        }
        return Ed25519Identity(seed)
    }

    private fun wrap(seed: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, aesKey())
        val iv = cipher.iv
        return iv + cipher.doFinal(seed)
    }

    private fun unwrap(box: ByteArray): ByteArray {
        require(box.size > CryptoConstants.KEYSTORE_GCM_IV_SIZE)
        val iv = box.copyOf(CryptoConstants.KEYSTORE_GCM_IV_SIZE)
        val payload = box.copyOfRange(CryptoConstants.KEYSTORE_GCM_IV_SIZE, box.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, aesKey(), GCMParameterSpec(128, iv))
        return cipher.doFinal(payload)
    }

    private fun aesKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        val existing = keyStore.getKey(ALIAS, null) as? SecretKey
        if (existing != null) return existing
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        generator.init(
            KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build(),
        )
        return generator.generateKey()
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ALIAS = "altu-identity-wrap"
        private const val BOX_NAME = "altu_ed25519.box"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
    }
}
