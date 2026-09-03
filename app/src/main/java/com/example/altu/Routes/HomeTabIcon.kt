package com.example.altu.Routes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeTabIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
) {
    Canvas(modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val heart = heartPath(w, h)

        val fill = if (selected) {
            Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE1BEE7),
                    Color(0xFFCE93D8),
                    Color(0xFFAB47BC),
                    Color(0xFF7B1FA2),
                ),
                center = Offset(w * 0.5f, h * 0.38f),
                radius = w * 0.72f,
            )
        } else {
            Brush.radialGradient(
                colors = listOf(
                    Color(0xFFB488A1),
                    Color(0xFF8E6A82),
                    Color(0xFF5A4458),
                ),
                center = Offset(w * 0.5f, h * 0.38f),
                radius = w * 0.72f,
            )
        }

        drawPath(heart, brush = fill)
        drawPath(
            path = heart,
            color = if (selected) Color(0xFFE8EAF0) else Color(0xFFACADAC),
            style = Stroke(
                width = 1.6.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )

        val cx = w * 0.5f
        val cy = h * 0.46f
        val crossColor = Color(0xFF1A0A1F)
        val armW = w * 0.09f
        val stemH = h * 0.42f
        val crossArmW = w * 0.34f
        val crossArmH = h * 0.11f

        drawPath(
            Path().apply {
                // vertical bar
                moveTo(cx - armW / 2f, cy - stemH * 0.45f)
                lineTo(cx + armW / 2f, cy - stemH * 0.45f)
                lineTo(cx + armW / 2f, cy + stemH * 0.55f)
                lineTo(cx - armW / 2f, cy + stemH * 0.55f)
                close()
            },
            color = crossColor,
        )
        drawPath(
            Path().apply {
                // horizontal bar
                moveTo(cx - crossArmW / 2f, cy - crossArmH * 0.15f)
                lineTo(cx + crossArmW / 2f, cy - crossArmH * 0.15f)
                lineTo(cx + crossArmW / 2f, cy + crossArmH * 0.85f)
                lineTo(cx - crossArmW / 2f, cy + crossArmH * 0.85f)
                close()
            },
            color = crossColor,
        )

        val sparkleColor = if (selected) Color.White else Color(0xFFD7D7D7)
        drawSparkle(
            center = Offset(w * 0.14f, h * 0.34f),
            radius = w * 0.085f,
            color = sparkleColor,
        )
        drawSparkle(
            center = Offset(w * 0.86f, h * 0.34f),
            radius = w * 0.075f,
            color = sparkleColor,
        )
    }
}

private fun heartPath(width: Float, height: Float): Path = Path().apply {
    val left = width * 0.08f
    val right = width * 0.92f
    val bottom = height * 0.96f
    val midX = width * 0.5f
    val midY = height * 0.32f

    moveTo(midX, midY)
    cubicTo(
        width * 0.18f, height * -0.02f,
        left, height * 0.18f,
        left, height * 0.42f,
    )
    cubicTo(
        left, height * 0.68f,
        width * 0.34f, height * 0.84f,
        midX, bottom,
    )
    cubicTo(
        width * 0.66f, height * 0.84f,
        right, height * 0.68f,
        right, height * 0.42f,
    )
    cubicTo(
        right, height * 0.18f,
        width * 0.82f, height * -0.02f,
        midX, midY,
    )
    close()
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSparkle(
    center: Offset,
    radius: Float,
    color: Color,
) {
    val path = Path()
    for (i in 0 until 8) {
        val angle = Math.PI * i / 4.0 - Math.PI / 2.0
        val r = if (i % 2 == 0) radius else radius * 0.28f
        val x = center.x + (cos(angle) * r).toFloat()
        val y = center.y + (sin(angle) * r).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color)
}
