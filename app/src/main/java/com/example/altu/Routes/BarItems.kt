package com.example.altu.Routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings

object BarItems {
    val items = listOf(
        BarItem(
            title = "Chats",
            image = Icons.Filled.Home,
            route = Routes.Home.route
        ),
        BarItem(
            title = "New Contact",
            image = Icons.Filled.Favorite,
            route = Routes.NewContact.route
        ),
        BarItem(
            title = "Settings",
            image = Icons.Filled.Settings,
            route = Routes.Settings.route
        )
    )
}