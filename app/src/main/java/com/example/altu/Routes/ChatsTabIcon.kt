package com.example.altu.Routes

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChatsTabIcon(
    selected: Boolean,
    unreadCount: Int = 0,
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp,
) {
    val batColor = if (selected) Color(0xFFD4B8D8) else Color(0xFFb488a1)
    val lineColor = if (selected) Color(0xFF4A2F52) else Color(0xFF3A2A40)
    val eyeColor = Color(0xFF1A0A1F)
    val badgeText = Color(0xFF1A0A1F)

    Box(
        modifier = modifier
            .height(iconSize)
            .aspectRatio(1.45f)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val bat = batPath(w, h)

            drawPath(bat, batColor)

            val bone = 1.15.dp.toPx()
            drawLine(lineColor, Offset(w * 0.42f, h * 0.38f), Offset(w * 0.12f, h * 0.28f), bone, StrokeCap.Round)
            drawLine(lineColor, Offset(w * 0.42f, h * 0.40f), Offset(w * 0.18f, h * 0.50f), bone, StrokeCap.Round)
            drawLine(lineColor, Offset(w * 0.58f, h * 0.38f), Offset(w * 0.88f, h * 0.28f), bone, StrokeCap.Round)
            drawLine(lineColor, Offset(w * 0.58f, h * 0.40f), Offset(w * 0.82f, h * 0.50f), bone, StrokeCap.Round)

            drawCircle(eyeColor, radius = 1.15.dp.toPx(), center = Offset(w * 0.455f, h * 0.26f))
            drawCircle(eyeColor, radius = 1.15.dp.toPx(), center = Offset(w * 0.545f, h * 0.26f))

            if (unreadCount > 0) {
                val badgeR = w * 0.13f
                val badgeCenter = Offset(w * 0.86f, h * 0.16f)
                drawCircle(batColor, radius = badgeR, center = badgeCenter)
                drawCircle(
                    color = lineColor.copy(alpha = 0.35f),
                    radius = badgeR,
                    center = badgeCenter,
                    style = Stroke(width = 0.8.dp.toPx()),
                )

                val label = if (unreadCount > 99) "99+" else unreadCount.toString()
                val paint = Paint().apply {
                    isAntiAlias = true
                    color = badgeText.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = badgeR * (if (label.length > 2) 1.05f else 1.28f)
                    typeface = Typeface.DEFAULT_BOLD
                }
                val textY = badgeCenter.y - (paint.descent() + paint.ascent()) / 2f
                drawContext.canvas.nativeCanvas.drawText(label, badgeCenter.x, textY, paint)
            }
        }
    }
}

private fun batPath(width: Float, height: Float): Path = Path().apply {
    val m = width * 0.5f

    // left ear
    moveTo(m, height * 0.20f)
    lineTo(width * 0.445f, height * 0.20f)
    lineTo(width * 0.405f, height * 0.04f)
    lineTo(width * 0.375f, height * 0.22f)

    // left wing top to outer tip
    lineTo(width * 0.22f, height * 0.16f)
    lineTo(width * 0.02f, height * 0.30f)

    // left wing: three scallops
    lineTo(width * 0.12f, height * 0.40f)
    lineTo(width * 0.06f, height * 0.62f)
    lineTo(width * 0.20f, height * 0.48f)
    lineTo(width * 0.16f, height * 0.78f)
    lineTo(width * 0.30f, height * 0.54f)
    lineTo(width * 0.34f, height * 0.70f)
    lineTo(width * 0.42f, height * 0.50f)

    // body point
    lineTo(m, height * 0.96f)

    // right wing: three scallops
    lineTo(width * 0.58f, height * 0.50f)
    lineTo(width * 0.66f, height * 0.70f)
    lineTo(width * 0.70f, height * 0.54f)
    lineTo(width * 0.84f, height * 0.78f)
    lineTo(width * 0.80f, height * 0.48f)
    lineTo(width * 0.94f, height * 0.62f)
    lineTo(width * 0.88f, height * 0.40f)

    // right wing tip and top
    lineTo(width * 0.98f, height * 0.30f)
    lineTo(width * 0.78f, height * 0.16f)

    // right ear
    lineTo(width * 0.625f, height * 0.22f)
    lineTo(width * 0.595f, height * 0.04f)
    lineTo(width * 0.555f, height * 0.20f)
    lineTo(m, height * 0.20f)
    close()
}
