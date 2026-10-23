package com.example.altu.api.dto

data class QueuedMessageDto(
    val messageId: String,
    val fromUserId: String,
    val toUserId: String,
    val chatId: String,
    val nonce: String,
    val ciphertext: String,
    val createdAt: String,
)
