package com.example.altu.ChatBar.Chat

import kotlin.math.abs

data class Message(
    val id: String,
    val content: String,
    val sender: String,
    val timestamp: String,
)

fun List<Message>.matchIndices(query: String): List<Int> {
    if (query.isBlank()) return emptyList()
    return mapIndexedNotNull { index, message ->
        if (message.content.contains(query, ignoreCase = true)) index else null
    }
}

fun List<Message>.nearestMatchIndex(query: String, fromIndex: Int): Int? {
    val matches = matchIndices(query)
    if (matches.isEmpty()) return null
    return matches.minWithOrNull(
        compareBy<Int> { abs(it - fromIndex) }.thenBy { if (it >= fromIndex) 0 else 1 }
    )
}

fun List<Message>.nextMatchIndex(query: String, fromIndex: Int): Int? {
    val matches = matchIndices(query)
    if (matches.isEmpty()) return null
    return matches.firstOrNull { it > fromIndex } ?: matches.first()
}

val VoidwalkerMessages: List<Message> = listOf(
    Message("m1", "aaaaaaaaaa", "voidwalker", "20:41"),
    Message("m2", "aaaaaaaaaa", "me", "20:42"),
    Message("m3", "aaaaaaaaaa", "voidwalker", "20:43"),
    Message("m4", "aaaaaaaaaa", "me", "20:45"),
    Message("m5", "aaaaaaaaaa", "voidwalker", "20:46"),
    Message("m6", "baaaaaaaaa", "me", "20:46"),
    Message("m7", "baaaaaaaaa", "voidwalker", "20:47"),
    Message("m8", "aaaaaaaaaa", "me", "20:51"),
    Message("m9", "aaaaaaaaaa", "voidwalker", "20:52"),
    Message("m10", "aaaaaaaaaa", "me", "20:53"),
    Message("m11", "aaaaaaaaaa", "voidwalker", "20:54"),
    Message("m12", "aaaaaaaaaa", "me", "20:55"),
    Message("m13", "aaaaaaaaaa", "voidwalker", "20:56"),
    Message("m14", "aaaaaaaaaa", "me", "20:57"),
    Message("m15", "aaaaaaaaaa", "voidwalker", "20:58"),
    Message("m16", "aaaaaaaaaa", "me", "20:59"),
    Message("m17", "aaaaaaaaaa", "voidwalker", "21:00"),
    Message("m18", "aaaaaaaaaa", "me", "21:01"),
    Message("m19", "aaaaaaaaaa", "voidwalker", "21:02"),
    Message("m20", "aaaaaaaaaa", "me", "21:03"),
    Message("m21", "aaaaaaaaaa", "voidwalker", "21:04"),
    Message("m22", "aaaaaaaaaa", "me", "21:05"),
    Message("m23", "aaaaaaaaaa", "voidwalker", "21:06"),
    Message("m24", "aaaaaaaaaa", "me", "21:07"),
    Message("m25", "aaaaaaaaaa", "voidwalker", "21:08"),
    Message("m26", "aaaaaaaaaa", "me", "21:09"),
    Message("m27", "aaaaaaaaaa", "voidwalker", "21:10"),
    Message("m28", "aaaaaaaaaa", "me", "21:11"),
    Message("m29", "aaaaaaaaaa", "voidwalker", "21:12"),
    Message("m30", "aaaaaaaaaa", "me", "21:13"),
    Message("m31", "aaaaaaaaaa", "voidwalker", "21:14"),
    Message("m32", "aaaaaaaaaa", "me", "21:15"),
)
