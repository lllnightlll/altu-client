package com.example.altu.Profile

data class Session(
    val userId: String,
    val tag: String,
    val avatarPath: String? = null,
)
