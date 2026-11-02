package com.example.altu.DataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.altu.DataBase.entity.QueuedMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageQueueDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(message: QueuedMessageEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(message: QueuedMessageEntity)

    @Query(
        """
        SELECT * FROM messages_queue
        WHERE to_user_id = :toUserId AND delivered_at IS NULL
        ORDER BY created_at ASC
        LIMIT :limit
        """
    )
    suspend fun findPendingForRecipient(toUserId: String, limit: Int): List<QueuedMessageEntity>

    @Query(
        """
        SELECT * FROM messages_queue
        WHERE from_user_id = :fromUserId
          AND to_user_id != :fromUserId
          AND delivered_at IS NULL
        ORDER BY created_at ASC
        LIMIT :limit
        """
    )
    suspend fun findPendingOutgoing(fromUserId: String, limit: Int): List<QueuedMessageEntity>

    @Query(
        """
        UPDATE messages_queue
        SET delivered_at = :deliveredAt
        WHERE message_id = :messageId
          AND to_user_id = :userId
          AND delivered_at IS NULL
        """
    )
    suspend fun markDelivered(userId: String, messageId: String, deliveredAt: Long): Int

    @Query(
        """
        UPDATE messages_queue
        SET delivered_at = :sentAt
        WHERE message_id = :messageId
          AND from_user_id = :fromUserId
          AND delivered_at IS NULL
        """
    )
    suspend fun markOutgoingSent(fromUserId: String, messageId: String, sentAt: Long): Int

    @Query(
        """
        UPDATE messages_queue
        SET delivered_at = :deliveredAt
        WHERE chat_id = :chatId
          AND to_user_id = :userId
          AND from_user_id != :userId
          AND delivered_at IS NULL
        """
    )
    suspend fun markChatRead(chatId: String, userId: String, deliveredAt: Long): Int

    @Query("DELETE FROM messages_queue WHERE delivered_at IS NOT NULL")
    suspend fun deleteDelivered(): Int

    @Query("SELECT * FROM messages_queue WHERE chat_id = :chatId ORDER BY created_at ASC")
    fun observeByChat(chatId: String): Flow<List<QueuedMessageEntity>>

    @Query("SELECT * FROM messages_queue")
    fun observeAll(): Flow<List<QueuedMessageEntity>>

    @Query("DELETE FROM messages_queue")
    suspend fun deleteAll()
}
