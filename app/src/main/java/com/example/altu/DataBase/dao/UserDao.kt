package com.example.altu.DataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.altu.DataBase.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE tag = :tag LIMIT 1")
    suspend fun findByTag(tag: String): UserEntity?

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    suspend fun findById(userId: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Long

    @Query("SELECT * FROM users WHERE user_id != :meId")
    fun observePeers(meId: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    fun observeById(userId: String): Flow<UserEntity?>

    @Query("UPDATE users SET public_key = :publicKey WHERE user_id = :userId")
    suspend fun updatePublicKey(userId: String, publicKey: ByteArray)
}
