package com.example.altu

import com.example.altu.Routes.NavBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.altu.Routes.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }
}

@Composable
fun Main() {
    val navController = rememberNavController()
    Column(Modifier.padding(8.dp)) {
        NavBar(navController = navController)
        NavHost(navController, startDestination = Routes.Home.route) {
            composable(Routes.Home.route) { Home() }
            composable(Routes.Chat.route) { Chat()  }
            composable(Routes.Settings.route) { Settings()  }
            composable(Routes.NewContact.route) { NewContact()  }
        }
    }
}

@Composable
fun Home(){
    Text("Home Page", fontSize = 30.sp)
}

@Composable
fun Chat(){
    Text("Chat Page", fontSize = 30.sp)
}

@Composable
fun Settings(){
    Text("Settings Page", fontSize = 30.sp)
}

@Composable
fun NewContact(){
    Text("Contact Page", fontSize = 30.sp)
}
