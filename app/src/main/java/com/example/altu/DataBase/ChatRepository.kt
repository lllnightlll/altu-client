package com.example.altu.DataBase

import com.example.altu.ChatBar.Chat.Message
import com.example.altu.ChatBar.ChatItem
import com.example.altu.DataBase.entity.QueuedMessageEntity
import com.example.altu.DataBase.entity.UserEntity
import com.example.altu.Profile.LocalUser
import com.example.altu.R
import com.example.altu.crypto.Ed25519Identity
import com.example.altu.crypto.MessageCipher
import com.example.altu.crypto.SendEnvelope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ChatRepository(
    private val database: AltuDatabase,
    private val identity: Ed25519Identity,
) {
    private val users = database.userDao()
    private val queue = database.messageQueueDao()
    private val cipher = MessageCipher(identity)
    private val clock = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun observeChats(): Flow<List<ChatItem>> {
        return combine(
            users.observeById(LocalUser.ID),
            users.observePeers(LocalUser.ID),
            queue.observeAll(),
        ) { me, peers, messages ->
            buildList {
                if (me != null) add(notesChatItem(messages))
                peers.forEach { peer -> add(peer.toChatItem(messages)) }
            }.sortedByDescending { chat ->
                messages.filter { it.chatId == chat.id }.maxOfOrNull { it.createdAt } ?: 0L
            }
        }
    }

    fun observeChat(chatId: String): Flow<ChatItem?> {
        return observeChats().map { chats -> chats.find { it.id == chatId } }
    }

    fun observeMessages(chatId: String): Flow<List<Message>> {
        return queue.observeByChat(chatId).combine(users.observeById(chatId)) { rows, peer ->
            val otherKey = peer?.publicKey ?: identity.publicKey
            rows.map { row ->
                val aad = MessageCipher.associatedData(row.fromUserId, row.toUserId, row.chatId)
                val content = cipher.decrypt(row.nonce, row.ciphertext, otherKey, aad)
                    ?: row.ciphertext.toString(Charsets.UTF_8)
                Message(
                    id = row.messageId,
                    content = content,
                    sender = if (row.fromUserId == LocalUser.ID) "me" else peer?.tag ?: row.fromUserId,
                    timestamp = clock.format(Date(row.createdAt)),
                )
            }
        }
    }

    suspend fun sendMessage(chatId: String, text: String): SendEnvelope? {
        val body = text.trim()
        if (body.isEmpty() || chatId.isBlank()) return null
        val peer = users.findById(chatId) ?: return null
        val now = System.currentTimeMillis()
        val aad = MessageCipher.associatedData(LocalUser.ID, peer.userId, peer.userId)
        val sealed = cipher.encrypt(body, peer.publicKey, aad)
        val envelope = SendEnvelope.create(
            identity = identity,
            fromUserId = LocalUser.ID,
            toUserId = peer.userId,
            chatId = peer.userId,
            sealed = sealed,
            timestampMillis = now,
        )
        queue.insert(
            QueuedMessageEntity(
                messageId = UUID.randomUUID().toString(),
                fromUserId = LocalUser.ID,
                toUserId = peer.userId,
                chatId = peer.userId,
                nonce = sealed.nonce,
                ciphertext = sealed.ciphertext,
                createdAt = now,
                deliveredAt = now,
            ),
        )
        return envelope
    }

    suspend fun deleteAllMessages() {
        queue.deleteAll()
    }

    suspend fun seedIfEmpty() {
        val existing = users.findById(LocalUser.ID)
        if (existing == null) {
            users.insert(
                UserEntity(
                    userId = LocalUser.ID,
                    tag = LocalUser.NOTES_TITLE,
                    publicKey = identity.publicKey,
                    createdAt = System.currentTimeMillis(),
                ),
            )
            return
        }
        if (!existing.publicKey.contentEquals(identity.publicKey)) {
            users.updatePublicKey(LocalUser.ID, identity.publicKey)
        }
    }

    private fun notesChatItem(messages: List<QueuedMessageEntity>): ChatItem {
        return chatItem(
            chatId = LocalUser.NOTES_CHAT_ID,
            nickname = LocalUser.NOTES_TITLE,
            avatarRes = LocalUser.avatarRes,
            messages = messages,
        )
    }

    private fun UserEntity.toChatItem(messages: List<QueuedMessageEntity>): ChatItem {
        return chatItem(
            chatId = userId,
            nickname = tag,
            avatarRes = R.drawable.sound_icon,
            messages = messages,
        )
    }

    private fun chatItem(
        chatId: String,
        nickname: String,
        avatarRes: Int,
        messages: List<QueuedMessageEntity>,
    ): ChatItem {
        val thread = messages.filter { it.chatId == chatId }
        val last = thread.maxByOrNull { it.createdAt }
        val unread = thread.count {
            it.toUserId == LocalUser.ID && it.fromUserId != LocalUser.ID && it.deliveredAt == null
        }
        return ChatItem(
            id = chatId,
            nickname = nickname,
            time = last?.let { clock.format(Date(it.createdAt)) } ?: "",
            unreadCount = unread,
            avatarRes = avatarRes,
        )
    }
}
