package com.example.altu.Routes

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Chat : Routes("chat/{chatId}") {
        fun create(chatId: String) = "chat/$chatId"
    }
    object Settings : Routes("settings")
    object NewContact : Routes("newContact")
}