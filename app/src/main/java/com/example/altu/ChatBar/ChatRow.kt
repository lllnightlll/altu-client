package com.example.altu.ChatBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder

@Composable
fun ChatRow(chat: ChatItem) {
    val accent = Color(0xFFACADAC)
    val windowShape = RoundedCornerShape(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .randomShadow()
            .steppedBorder(width = 1.dp, color = accent, shape = windowShape)
            .background(Color(0xFF070809), windowShape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeartAvatar(
            avatarRes = chat.avatarRes,
            contentDescription = chat.nickname,
            height = 54.dp,
            widthRatio = 1.45f,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = chat.nickname,
            color = accent,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.height(54.dp),
        ) {
            Text(
                text = chat.time,
                color = accent.copy(alpha = 0.7f),
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (chat.unreadCount > 0) {
                UnreadBadge(count = chat.unreadCount)
            } else {
                Spacer(modifier = Modifier.size(18.dp))
            }
        }
    }
}