package com.example.altu.DataBase

import android.net.Uri
import com.example.altu.Auth.TagRules
import com.example.altu.ChatBar.Chat.Message
import com.example.altu.ChatBar.ChatItem
import com.example.altu.DataBase.entity.QueuedMessageEntity
import com.example.altu.DataBase.entity.UserEntity
import com.example.altu.Profile.AvatarStore
import com.example.altu.Profile.LocalUser
import com.example.altu.Profile.Session
import com.example.altu.Profile.SessionStore
import com.example.altu.R
import com.example.altu.crypto.Ed25519Identity
import com.example.altu.crypto.MessageCipher
import com.example.altu.crypto.SendEnvelope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepository(
    private val database: AltuDatabase,
    private val identity: Ed25519Identity,
    private val sessionStore: SessionStore,
    private val avatarStore: AvatarStore,
) {
    private val users = database.userDao()
    private val queue = database.messageQueueDao()
    private val cipher = MessageCipher(identity)
    private val clock = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun observeChats(): Flow<List<ChatItem>> {
        return sessionStore.session.flatMapLatest { session ->
            val meId = session?.userId ?: return@flatMapLatest flowOf(emptyList())
            combine(users.observePeers(meId), queue.observeAll()) { peers, messages ->
                buildList {
                    add(notesChatItem(session, messages))
                    peers.forEach { peer -> add(peer.toChatItem(meId, messages)) }
                }.sortedByDescending { chat ->
                    messages.filter { it.chatId == chat.id }.maxOfOrNull { it.createdAt } ?: 0L
                }
            }
        }
    }

    fun observeChat(chatId: String): Flow<ChatItem?> {
        return observeChats().map { chats -> chats.find { it.id == chatId } }
    }

    fun observeMessages(chatId: String): Flow<List<Message>> {
        return sessionStore.session.flatMapLatest { session ->
            val meId = session?.userId ?: return@flatMapLatest flowOf(emptyList())
            queue.observeByChat(chatId).combine(users.observeById(chatId)) { rows, peer ->
                val otherKey = peer?.publicKey ?: identity.publicKey
                rows.map { row ->
                    val aad = MessageCipher.associatedData(row.fromUserId, row.toUserId, row.chatId)
                    val content = cipher.decrypt(row.nonce, row.ciphertext, otherKey, aad)
                        ?: row.ciphertext.toString(Charsets.UTF_8)
                    Message(
                        id = row.messageId,
                        content = content,
                        sender = if (row.fromUserId == meId) "me" else peer?.tag ?: row.fromUserId,
                        timestamp = clock.format(Date(row.createdAt)),
                    )
                }
            }
        }
    }

    suspend fun register(tag: String, avatarUri: Uri? = null): Result<Unit> {
        val clean = TagRules.normalize(tag)
        TagRules.errorOrNull(clean)?.let { return Result.failure(IllegalArgumentException(it)) }
        if (sessionStore.isRegistered()) return Result.success(Unit)
        val userId = UUID.randomUUID().toString()
        val avatarPath = avatarUri?.let { avatarStore.save(it) }
        users.insert(
            UserEntity(
                userId = userId,
                tag = clean,
                publicKey = identity.publicKey,
                createdAt = System.currentTimeMillis(),
            ),
        )
        sessionStore.save(userId, clean, avatarPath)
        return Result.success(Unit)
    }

    suspend fun deleteAccount() {
        queue.deleteAll()
        users.deleteAll()
        avatarStore.deleteAll()
        sessionStore.clear()
    }

    suspend fun sendMessage(chatId: String, text: String): SendEnvelope? {
        val body = text.trim()
        val meId = sessionStore.userId() ?: return null
        if (body.isEmpty() || chatId.isBlank()) return null
        val peer = users.findById(chatId) ?: return null
        val now = System.currentTimeMillis()
        val aad = MessageCipher.associatedData(meId, peer.userId, peer.userId)
        val sealed = cipher.encrypt(body, peer.publicKey, aad)
        val envelope = SendEnvelope.create(
            identity = identity,
            fromUserId = meId,
            toUserId = peer.userId,
            chatId = peer.userId,
            sealed = sealed,
            timestampMillis = now,
        )
        queue.insert(
            QueuedMessageEntity(
                messageId = UUID.randomUUID().toString(),
                fromUserId = meId,
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

    suspend fun ensureLocalUser() {
        val session = sessionStore.session.value ?: return
        val existing = users.findById(session.userId)
        if (existing == null) {
            users.insert(
                UserEntity(
                    userId = session.userId,
                    tag = session.tag,
                    publicKey = identity.publicKey,
                    createdAt = System.currentTimeMillis(),
                ),
            )
            return
        }
        if (!existing.publicKey.contentEquals(identity.publicKey)) {
            users.updatePublicKey(session.userId, identity.publicKey)
        }
    }

    private fun notesChatItem(session: Session, messages: List<QueuedMessageEntity>): ChatItem {
        return chatItem(
            chatId = session.userId,
            nickname = LocalUser.NOTES_TITLE,
            avatarRes = LocalUser.avatarRes,
            avatarPath = session.avatarPath,
            meId = session.userId,
            messages = messages,
        )
    }

    private fun UserEntity.toChatItem(meId: String, messages: List<QueuedMessageEntity>): ChatItem {
        return chatItem(
            chatId = userId,
            nickname = tag,
            avatarRes = R.drawable.sound_icon,
            meId = meId,
            messages = messages,
        )
    }

    private fun chatItem(
        chatId: String,
        nickname: String,
        avatarRes: Int,
        meId: String,
        messages: List<QueuedMessageEntity>,
        avatarPath: String? = null,
    ): ChatItem {
        val thread = messages.filter { it.chatId == chatId }
        val last = thread.maxByOrNull { it.createdAt }
        val unread = thread.count {
            it.toUserId == meId && it.fromUserId != meId && it.deliveredAt == null
        }
        return ChatItem(
            id = chatId,
            nickname = nickname,
            time = last?.let { clock.format(Date(it.createdAt)) } ?: "",
            unreadCount = unread,
            avatarRes = avatarRes,
            avatarPath = avatarPath,
        )
    }
}
