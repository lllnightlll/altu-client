package com.example.altu.DataBase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["tag"], unique = true)],
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "tag")
    val tag: String,
    @ColumnInfo(name = "public_key")
    val publicKey: ByteArray,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserEntity) return false
        return userId == other.userId &&
            tag == other.tag &&
            publicKey.contentEquals(other.publicKey) &&
            createdAt == other.createdAt
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + publicKey.contentHashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
