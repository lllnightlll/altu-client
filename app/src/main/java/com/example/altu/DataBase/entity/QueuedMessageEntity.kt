package com.example.altu.DataBase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages_queue",
    indices = [
        Index(value = ["to_user_id", "delivered_at", "created_at"]),
    ],
)
data class QueuedMessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val messageId: String,
    @ColumnInfo(name = "from_user_id")
    val fromUserId: String,
    @ColumnInfo(name = "to_user_id")
    val toUserId: String,
    @ColumnInfo(name = "chat_id")
    val chatId: String,
    @ColumnInfo(name = "nonce")
    val nonce: ByteArray,
    @ColumnInfo(name = "ciphertext")
    val ciphertext: ByteArray,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "delivered_at")
    val deliveredAt: Long?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QueuedMessageEntity) return false
        return messageId == other.messageId &&
            fromUserId == other.fromUserId &&
            toUserId == other.toUserId &&
            chatId == other.chatId &&
            nonce.contentEquals(other.nonce) &&
            ciphertext.contentEquals(other.ciphertext) &&
            createdAt == other.createdAt &&
            deliveredAt == other.deliveredAt
    }

    override fun hashCode(): Int {
        var result = messageId.hashCode()
        result = 31 * result + fromUserId.hashCode()
        result = 31 * result + toUserId.hashCode()
        result = 31 * result + chatId.hashCode()
        result = 31 * result + nonce.contentHashCode()
        result = 31 * result + ciphertext.contentHashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + (deliveredAt?.hashCode() ?: 0)
        return result
    }
}
