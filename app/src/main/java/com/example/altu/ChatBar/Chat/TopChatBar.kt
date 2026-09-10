package com.example.altu.ChatBar.Chat

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.altu.ChatBar.HeartAvatar
import com.example.altu.R
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

@Composable
fun TopChatBar(
    nickname: String,
    @DrawableRes avatarRes: Int = R.drawable.sound_icon,
    modifier: Modifier = Modifier,
) {
    val accent = Color(0xFFACADAC)
    val barShape = RoundedCornerShape(64.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .randomShadow()
            .steppedBorder(width = 1.dp, color = accent, shape = barShape)
            .background(Color(0xFF070809), barShape)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeartAvatar(
            avatarRes = avatarRes,
            contentDescription = nickname,
            height = 32.dp,
            widthRatio = 1.45f,
            borderWidth = 1.dp,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = nickname,
            color = accent,
            fontFamily = GothicFont,
            fontSize = 27.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
    }
}
