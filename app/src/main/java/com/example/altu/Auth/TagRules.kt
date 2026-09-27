package com.example.altu.Auth

object TagRules {
    private val pattern = Regex("^[A-Za-z0-9_]{3,24}$")

    fun normalize(raw: String): String = raw.trim()

    fun errorOrNull(raw: String): String? {
        val tag = normalize(raw)
        if (tag.isEmpty()) return "Enter a tag"
        if (!pattern.matches(tag)) return "3-24 letters, digits or _"
        return null
    }
}
