package com.example.altu.crypto

import java.math.BigInteger
import java.security.MessageDigest

object Curve25519Convert {
    private val FIELD_P = BigInteger.ONE.shiftLeft(255).subtract(BigInteger.valueOf(19))

    fun privateKey(ed25519Seed: ByteArray): ByteArray {
        require(ed25519Seed.size == CryptoConstants.ED25519_SECRET_SEED_SIZE)
        val hash = MessageDigest.getInstance("SHA-512").digest(ed25519Seed)
        hash[0] = (hash[0].toInt() and 248).toByte()
        hash[31] = (hash[31].toInt() and 127).toByte()
        hash[31] = (hash[31].toInt() or 64).toByte()
        return hash.copyOf(CryptoConstants.X25519_KEY_SIZE)
    }

    fun publicKey(ed25519Public: ByteArray): ByteArray {
        require(ed25519Public.size == CryptoConstants.ED25519_PUBLIC_KEY_SIZE)
        val yBytes = ed25519Public.copyOf()
        yBytes[31] = (yBytes[31].toInt() and 0x7F).toByte()
        val y = fromLittleEndian(yBytes)
        val onePlusY = BigInteger.ONE.add(y).mod(FIELD_P)
        val oneMinusY = BigInteger.ONE.subtract(y).mod(FIELD_P)
        val u = onePlusY.multiply(oneMinusY.modInverse(FIELD_P)).mod(FIELD_P)
        return toLittleEndian(u, CryptoConstants.X25519_KEY_SIZE)
    }

    private fun fromLittleEndian(bytes: ByteArray): BigInteger {
        return BigInteger(1, bytes.reversedArray())
    }

    private fun toLittleEndian(value: BigInteger, size: Int): ByteArray {
        val raw = value.toByteArray()
        val unsigned = if (raw.isNotEmpty() && raw[0] == 0.toByte()) {
            raw.copyOfRange(1, raw.size)
        } else {
            raw
        }
        val little = unsigned.reversedArray()
        return if (little.size >= size) {
            little.copyOf(size)
        } else {
            little.copyOf(size)
        }
    }
}
