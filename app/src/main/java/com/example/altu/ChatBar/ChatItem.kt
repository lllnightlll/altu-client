package com.example.altu.ChatBar

import androidx.annotation.DrawableRes
import com.example.altu.R

data class ChatItem(
    val id: String,
    val nickname: String,
    val time: String,
    val unreadCount: Int,
    @DrawableRes val avatarRes: Int = R.drawable.sound_icon,
)
