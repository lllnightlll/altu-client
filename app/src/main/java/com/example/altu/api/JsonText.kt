package com.example.altu.api

internal object JsonText {
    fun string(value: String): String {
        val escaped = value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        return "\"$escaped\""
    }

    fun stringArray(values: List<String>): String {
        return values.joinToString(prefix = "[", postfix = "]") { string(it) }
    }
}
