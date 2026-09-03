package com.example.altu.Routes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.altu.SteppedBorder.steppedBorder
import kotlin.random.Random

@Composable
fun NavBar(
    navController: NavController,
    lastOpenChatId: String? = null,
    onReturnToHomeList: () -> Unit = {},
) {
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
            .steppedBorder(width = 0.5.dp, color = Color(0xFFFFFFFF), shape = RoundedCornerShape(32.dp))
            //.border(0.25.dp, Color(0xFFFFFFFF), barShape)
            .clip(barShape),
        containerColor = Color(0xFF070809),
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        BarItems.items.forEach { navItem ->
            val isHomeTab = navItem.route == Routes.Home.route
            val selected = when {
                isHomeTab -> currentRoute == Routes.Home.route || currentRoute?.startsWith("chat/") == true
                else -> currentRoute == navItem.route
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (isHomeTab) {
                        when {
                            currentRoute?.startsWith("chat/") == true -> {
                                navController.popBackStack(
                                    route = Routes.Home.route,
                                    inclusive = false,
                                    saveState = true,
                                )
                                onReturnToHomeList()
                            }
                            currentRoute == Routes.Home.route -> Unit
                            lastOpenChatId != null -> {
                                navController.navigate(Routes.Chat.create(lastOpenChatId)) {
                                    popUpTo(Routes.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            else -> {
                                val returnedToHome = navController.popBackStack(
                                    route = Routes.Home.route,
                                    inclusive = false,
                                    saveState = true,
                                )
                                if (!returnedToHome) {
                                    navController.navigate(Routes.Home.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        }
                    } else {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    when {
                        isHomeTab -> HomeTabIcon(selected = selected)
                        navItem.route == Routes.Settings.route -> SettingsTabIcon(selected = selected)
                        else -> Icon(
                            imageVector = navItem.image,
                            contentDescription = navItem.title
                        )
                    }
                },
                label = {
                    Text(text = navItem.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFFFFFF),
                    selectedTextColor = Color(0xFFFFFFFF),
                    unselectedIconColor = Color(0xFFb488a1),
                    unselectedTextColor = Color(0xFFb488a1),
                    indicatorColor = Color(0xFF1C1E21)
                )
            )
        }
    }
}