package com.example.altu.ChatBar.Chat

data class Message(
    val id: String,
    val content: String,
    val sender: String,
    val timestamp: String,
)

object Messages {
    fun forChat(chatId: String?): List<Message> {
        val notMe = chatId ?: "notMe"
        return listOf(
            Message("m1", "aaaaaaaaaa", notMe, "20:41"),
            Message("m2", "aaaaaaaaaa", "me", "20:42"),
            Message("m3", "aaaaaaaaaa", notMe, "20:43"),
            Message("m4", "aaaaaaaaaa", "me", "20:45"),
            Message("m5", "aaaaaaaaaa", notMe, "20:46"),
            Message("m6", "aaaaaaaaaa", "me", "20:46"),
            Message("m7", "aaaaaaaaaa", notMe, "20:47"),
            Message("m8", "aaaaaaaaaa", "me", "20:51"),
            Message("m9", "aaaaaaaaaa", notMe, "20:52"),
            Message("m10", "aaaaaaaaaa", "me", "20:53"),
        )
    }
}
