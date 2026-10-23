package com.example.altu.api

object ServerPaths {
    const val REGISTER = "/register"
    const val SEND = "/send"
    const val RECEIVED = "/receivedMessages"

    fun messages(userId: String): String = "/messages/$userId"

    fun getMessagesPayload(userId: String): String = "GET /messages/$userId"
}
