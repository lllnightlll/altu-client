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
