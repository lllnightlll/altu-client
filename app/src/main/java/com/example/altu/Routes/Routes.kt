package com.example.altu.Routes

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Chat : Routes("chat")
    object Settings : Routes("settings")
    object NewContact : Routes("newContact")
}