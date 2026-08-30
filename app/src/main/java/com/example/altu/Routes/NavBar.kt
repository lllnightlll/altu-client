package com.example.altu.Routes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.random.Random

@Composable
fun NavBar(navController: NavController) {
    val stepHeights = remember {
        List(5) { Random.nextInt(2, 11).toFloat() }
    }
    val cornerPx = with(LocalDensity.current) { 32.dp.toPx() }
    val barShape = GenericShape { size, _ ->
        val segments = stepHeights.size
        val segmentWidth = size.width / segments
        val stepped = Path().apply {
            moveTo(0f, stepHeights[0])
            lineTo(segmentWidth, stepHeights[0])
            for (i in 1 until segments) {
                lineTo(i * segmentWidth, stepHeights[i])
                lineTo((i + 1) * segmentWidth, stepHeights[i])
            }
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        val rounded = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, size.width, size.height),
                    cornerRadius = CornerRadius(cornerPx, cornerPx)
                )
            )
        }
        op(stepped, rounded, PathOperation.Intersect)
    }

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.25.dp, Color(0xFFFFFFFF), barShape)
            .clip(barShape),
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