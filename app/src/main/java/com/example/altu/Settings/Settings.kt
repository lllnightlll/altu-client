package com.example.altu.Settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder

@Composable
fun Settings(
    onBackToChats: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        SettingsBar(onBackToChats = onBackToChats)
    }
}

@Composable
fun SettingsBar(
    onBackToChats: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val accent = Color(0xFFACADAC)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .randomShadow()
                .steppedBorder(
                    width = 1.dp,
                    color = accent,
                    shape = CircleShape,
                    fillColor = Color(0xFF070809),
                )
                .clickable(onClick = onBackToChats),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Chats",
                tint = accent,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
