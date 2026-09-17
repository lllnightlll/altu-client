package com.example.altu.NewContact

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder

@Composable
fun QrFrame(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Filled.QrCode,
            contentDescription = "QR placeholder",
            tint = Color(0xFFACADAC).copy(alpha = 0.35f),
            modifier = Modifier.size(128.dp),
        )
    },
) {
    val accent = Color(0xFFACADAC)
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1f)
            .randomShadow()
            .steppedBorder(
                width = 1.5.dp,
                color = accent,
                shape = shape,
                fillColor = Color(0xFF070809),
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
