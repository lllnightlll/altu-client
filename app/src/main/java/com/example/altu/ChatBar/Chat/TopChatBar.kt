package com.example.altu.ChatBar.Chat

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
    onHomeClick: () -> Unit = {},
    onFindNearestMessageClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val accent = Color(0xFFACADAC)
    val barShape = RoundedCornerShape(64.dp)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .randomShadow()
                .steppedBorder(width = 1.dp, color = accent, shape = CircleShape, fillColor = Color(0xFF070809))
                //.background(Color(0xFF070809), CircleShape)
                .clickable(onClick = onHomeClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Home",
                tint = accent,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Row(
            modifier = Modifier
                .weight(1f)
                .randomShadow()
                .steppedBorder(width = 1.dp, color = accent, shape = barShape, fillColor = Color(0xFF070809))
                //.background(Color(0xFF070809), barShape)
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

        Spacer(modifier = Modifier.size(8.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .randomShadow()
                .steppedBorder(width = 1.dp, color = accent, shape = CircleShape, fillColor = Color(0xFF070809))
                //.background(Color(0xFF070809), CircleShape)
                .clickable(onClick = onFindNearestMessageClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Find nearest message",
                tint = accent,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
