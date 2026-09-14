package com.example.altu.ChatBar.Chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

@Composable
fun MessageBubble(
    message: Message,
    highlighted: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val fromMe = message.sender == "me"
    val accent = if (highlighted) {
        Color(0xFFEC407A)
    } else if (fromMe) {
        Color(0xFF070809)
    } else {
        Color(0xFFACADAC)
    }
    val bubbleColor = if (fromMe) Color(0xFFACADAC) else Color(0xFF070809)
    val bubbleShape = RoundedCornerShape(16.dp)
    val contentStyle = TextStyle(
        color = accent,
        fontFamily = GothicFont,
        fontSize = 20.sp,
        lineHeight = 22.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    )
    val timeStyle = TextStyle(
        color = accent.copy(alpha = 0.55f),
        fontFamily = GothicFont,
        fontSize = 13.sp,
        lineHeight = 14.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .randomShadow()
                .steppedBorder(
                    width = 1.dp,
                    color = accent,
                    shape = bubbleShape,
                    fillColor = bubbleColor,
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = if (fromMe) Alignment.End else Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = message.content,
                style = contentStyle,
                modifier = Modifier.offset(y = (-2).dp),
            )
            Text(
                text = message.timestamp,
                style = timeStyle,
                modifier = Modifier.offset(y = 2.dp),
            )
        }
    }
}
