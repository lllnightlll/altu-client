package com.example.altu.DataBase

import com.example.altu.ChatBar.Chat.Message
import com.example.altu.ChatBar.ChatItem
import com.example.altu.DataBase.entity.QueuedMessageEntity
import com.example.altu.DataBase.entity.UserEntity
import com.example.altu.Profile.LocalUser
import com.example.altu.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ChatRepository(
    private val database: AltuDatabase,
) {
    private val users = database.userDao()
    private val queue = database.messageQueueDao()
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
            rows.map { row ->
                Message(
                    id = row.messageId,
                    content = row.ciphertext.toString(Charsets.UTF_8),
                    sender = if (row.fromUserId == LocalUser.ID) "me" else peer?.tag ?: row.fromUserId,
                    timestamp = clock.format(Date(row.createdAt)),
                )
            }
        }
    }

    suspend fun sendMessage(chatId: String, text: String) {
        val body = text.trim()
        if (body.isEmpty() || chatId.isBlank()) return
        val peer = users.findById(chatId) ?: return
        val now = System.currentTimeMillis()
        queue.insert(
            QueuedMessageEntity(
                messageId = UUID.randomUUID().toString(),
                fromUserId = LocalUser.ID,
                toUserId = peer.userId,
                chatId = peer.userId,
                nonce = ByteArray(1),
                ciphertext = body.toByteArray(Charsets.UTF_8),
                createdAt = now,
                deliveredAt = now,
            ),
        )
    }

    suspend fun deleteAllMessages() {
        queue.deleteAll()
    }

    suspend fun seedIfEmpty() {
        if (users.count() > 0) return
        users.insert(
            UserEntity(
                userId = LocalUser.ID,
                tag = LocalUser.NOTES_TITLE,
                publicKey = ByteArray(ED25519_KEY_SIZE),
                createdAt = System.currentTimeMillis(),
            ),
        )
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

    companion object {
        private const val ED25519_KEY_SIZE = 32
    }
}
