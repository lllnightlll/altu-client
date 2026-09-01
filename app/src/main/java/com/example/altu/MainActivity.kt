package com.example.altu

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.ChatBar.ChatBar
import com.example.altu.ChatBar.ChatItem
import com.example.altu.ChatBar.ChatItems
import com.example.altu.SearchBar.SearchBar
import com.example.altu.SoundBar.SoundBar
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.altu.Routes.NavBar
import com.example.altu.Routes.Routes

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.isNavigationBarContrastEnforced = false
        setContent {
            Main()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    var lastOpenChatId by remember { mutableStateOf<String?>(null) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF6650a4),
        bottomBar = {
            NavBar(
                navController = navController,
                lastOpenChatId = lastOpenChatId,
                onReturnToHomeList = { lastOpenChatId = null },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            composable(Routes.Home.route) {
                Home(
                    onChatClick = { chat ->
                        lastOpenChatId = chat.id
                        navController.navigate(Routes.Chat.create(chat.id))
                    }
                )
            }
            composable(
                route = Routes.Chat.route,
                arguments = listOf(navArgument("chatId") { type = NavType.StringType }),
            ) { entry ->
                Chat(chatId = entry.arguments?.getString("chatId"))
            }
            composable(Routes.Settings.route) { Settings() }
            composable(Routes.NewContact.route) { NewContact() }
        }
    }
}

@Composable
fun Home(
    onChatClick: (ChatItem) -> Unit = {},
) {
    var query by remember { mutableStateOf("") }
    val filteredChats = remember(query) {
        if (query.isBlank()) {
            ChatItems.items
        } else {
            ChatItems.items.filter { it.nickname.contains(query, ignoreCase = true) }
        }
    }

    Column(Modifier.fillMaxSize()) {
        SoundBar(title = "The Crowds")
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
        ChatBar(
            chats = filteredChats,
            onChatClick = onChatClick,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun Chat(chatId: String? = null) {
    val chat = ChatItems.items.find { it.id == chatId }
    Text(
        text = chat?.nickname?.let { "Chat with $it" } ?: "Chat Page",
        fontSize = 30.sp,
    )
}

@Composable
fun Settings(){
    Text("Settings Page", fontSize = 30.sp)
}

@Composable
fun NewContact(){
    Text("Contact Page", fontSize = 30.sp)
}
