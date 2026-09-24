package com.example.altu.ChatBar.Chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

private val ComposerMinHeight = 48.dp
private val ComposerMaxHeight = 140.dp
private const val ComposerMaxLines = 5

/**
 * Нижняя строка чата: поле растёт вниз при переносе текста, кнопка отправки снизу справа.
 * Пустую строку кнопка не отправляет.
 */
@Composable
fun MessageComposer(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = Color(0xFFACADAC)
    val ink = Color(0xFF070809)
    val fieldShape = RoundedCornerShape(24.dp)
    val canSend = text.isNotBlank()
    val fieldScroll = rememberScrollState()
    val fieldStyle = TextStyle(
        color = accent,
        fontFamily = GothicFont,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = ComposerMinHeight, max = ComposerMaxHeight)
                .randomShadow()
                .steppedBorder(width = 1.dp, color = accent, shape = fieldShape, fillColor = ink)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.TopStart,
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                singleLine = false,
                maxLines = ComposerMaxLines,
                cursorBrush = SolidColor(accent),
                textStyle = fieldStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(fieldScroll),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Message",
                                style = fieldStyle.copy(color = accent.copy(alpha = 0.45f)),
                            )
                        }
                        innerTextField()
                    }
                },
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(ComposerMinHeight)
                .randomShadow()
                .steppedBorder(
                    width = 1.dp,
                    color = if (canSend) accent else accent.copy(alpha = 0.35f),
                    shape = CircleShape,
                    fillColor = ink,
                )
                .clickable(enabled = canSend, onClick = onSend),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = if (canSend) accent else accent.copy(alpha = 0.35f),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
