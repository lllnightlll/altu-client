package com.example.altu.Routes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavBar(navController: NavController){
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFFFFFF), RoundedCornerShape(24.dp)),
        containerColor = Color(0xFF070809),
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BarItems.items.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFEC407A),
                    selectedTextColor = Color(0xFFEC407A),
                    unselectedIconColor = Color(0xFFb488a1),
                    unselectedTextColor = Color(0xFFb488a1),
                    indicatorColor = Color(0xFF1C1E21)
                )
            )
        }
    }
}