package com.example.altu.Routes

import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun NavBar(navController: NavController){
    Row(
        Modifier.fillMaxWidth().padding(bottom = 8.dp)){
        Text("Home",
            Modifier
                .weight(0.33f)
                .clickable { navController.navigate(Routes.Home.route) }, fontSize = 22.sp, color= Color(0xFF6650a4))
        Text("Chat",
            Modifier
                .weight(0.33f)
                .clickable { navController.navigate(Routes.Chat.route) }, fontSize = 22.sp, color= Color(0xFF6650a4))
        Text("Settings",
            Modifier
                .weight(0.33f)
                .clickable { navController.navigate(Routes.Settings.route) }, fontSize = 22.sp, color= Color(0xFF6650a4))
        Text("New Contact",
            Modifier
                .weight(0.33f)
                .clickable { navController.navigate(Routes.NewContact.route) }, fontSize = 22.sp, color= Color(0xFF6650a4))
    }
}