package com.example.altu.NewContact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.altu.SoundBar.randomShadow
import com.example.altu.SteppedBorder.steppedBorder
import com.example.altu.ui.theme.GothicFont

@Composable
fun QrFrame(
    payload: String?,
    modifier: Modifier = Modifier,
) {
    val accent = Color(0xFFACADAC)
    val ink = Color(0xFF070809)
    val shape = RoundedCornerShape(20.dp)
    val sizePx = with(LocalDensity.current) { 280.dp.roundToPx() }
    val qrBitmap = remember(payload, sizePx) {
        payload?.takeIf { it.isNotBlank() }?.let { QrBitmap.encode(it, sizePx, ink, accent) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .aspectRatio(1f)
            .randomShadow()
            .steppedBorder(
                width = 1.5.dp,
                color = accent,
                shape = shape,
                fillColor = ink,
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (qrBitmap != null) {
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "Contact QR",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text(
                text = "Sign in to show QR",
                color = accent.copy(alpha = 0.45f),
                fontFamily = GothicFont,
                fontSize = 18.sp,
            )
        }
    }
}
