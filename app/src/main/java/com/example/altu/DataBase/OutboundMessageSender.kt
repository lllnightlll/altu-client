package com.example.altu.DataBase

import com.example.altu.DataBase.dao.MessageQueueDao
import com.example.altu.DataBase.entity.QueuedMessageEntity
import com.example.altu.api.AltuApi
import com.example.altu.crypto.Ed25519Identity
import com.example.altu.crypto.SealedMessage
import com.example.altu.crypto.SendEnvelope
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

class OutboundMessageSender(
    private val queue: MessageQueueDao,
    private val api: AltuApi,
    private val identity: Ed25519Identity,
) {
    suspend fun persistAndDispatch(
        meId: String,
        peerId: String,
        chatId: String,
        sealed: SealedMessage,
        createdAt: Long,
    ): SendEnvelope {
        val envelope = envelopeOf(meId, peerId, chatId, sealed, createdAt)
        val messageId = UUID.randomUUID().toString()
        val notes = peerId == meId
        queue.insert(
            QueuedMessageEntity(
                messageId = messageId,
                fromUserId = meId,
                toUserId = peerId,
                chatId = chatId,
                nonce = sealed.nonce,
                ciphertext = sealed.ciphertext,
                createdAt = createdAt,
                deliveredAt = if (notes) createdAt else null,
            ),
        )
        if (!notes) {
            trySend(meId, messageId, envelope)
        }
        return envelope
    }

    suspend fun flush(meId: String) {
        val pending = queue.findPendingOutgoing(meId, FLUSH_BATCH)
        for (row in pending) {
            val envelope = envelopeOf(
                meId = meId,
                peerId = row.toUserId,
                chatId = row.chatId,
                sealed = SealedMessage(nonce = row.nonce, ciphertext = row.ciphertext),
                timestampMillis = System.currentTimeMillis(),
            )
            val sent = trySend(meId, row.messageId, envelope)
            if (!sent) break
        }
    }

    private suspend fun trySend(meId: String, messageId: String, envelope: SendEnvelope): Boolean {
        return try {
            api.send(envelope)
            queue.markOutgoingSent(meId, messageId, System.currentTimeMillis())
            true
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            false
        }
    }

    private fun envelopeOf(
        meId: String,
        peerId: String,
        chatId: String,
        sealed: SealedMessage,
        timestampMillis: Long,
    ): SendEnvelope {
        return SendEnvelope.create(
            identity = identity,
            fromUserId = meId,
            toUserId = peerId,
            chatId = chatId,
            sealed = sealed,
            timestampMillis = timestampMillis,
        )
    }

    companion object {
        private const val FLUSH_BATCH = 32
    }
}
