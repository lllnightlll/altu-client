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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SettingsTabIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
) {
    Canvas(modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        val metal = if (selected) Color(0xFFCFD4DA) else Color(0xFF8E949B)
        val metalDark = if (selected) Color(0xFF6B7078) else Color(0xFF4A4E54)
        val wingFill = if (selected) Color(0xFF2A2D33) else Color(0xFF1A1C20)
        val heartFill = Brush.radialGradient(
            colors = if (selected) {
                listOf(Color(0xFF3A3D45), Color(0xFF15171B), Color(0xFF050607))
            } else {
                listOf(Color(0xFF2A2C31), Color(0xFF101214), Color(0xFF050607))
            },
            center = Offset(w * 0.5f, h * 0.42f),
            radius = w * 0.55f,
        )

        drawWing(
            left = true,
            width = w,
            height = h,
            fill = wingFill,
            bone = metalDark,
        )
        drawWing(
            left = false,
            width = w,
            height = h,
            fill = wingFill,
            bone = metalDark,
        )

        val heart = settingsHeartPath(w, h)
        drawPath(heart, brush = heartFill)
        drawPath(
            path = heart,
            color = metal,
            style = Stroke(
                width = 1.8.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            ),
        )

        val studs = listOf(
            Offset(w * 0.20f, h * 0.22f),
            Offset(w * 0.50f, h * 0.16f),
            Offset(w * 0.80f, h * 0.22f),
            Offset(w * 0.10f, h * 0.48f),
            Offset(w * 0.90f, h * 0.48f),
            Offset(w * 0.30f, h * 0.78f),
            Offset(w * 0.70f, h * 0.78f),
            Offset(w * 0.50f, h * 0.90f),
        )
        studs.forEach { center ->
            drawCircle(color = metalDark, radius = 1.1.dp.toPx(), center = center)
            drawCircle(color = metal, radius = 0.55.dp.toPx(), center = center)
        }

        val cx = w * 0.5f
        val cy = h * 0.48f
        val crossColor = if (selected) Color(0xFFE8EAF0) else Color(0xFFACADAC)
        val armW = w * 0.055f
        val stemH = h * 0.28f
        val crossArmW = w * 0.20f
        val crossArmH = h * 0.055f

        drawPath(
            Path().apply {
                moveTo(cx - armW / 2f, cy - stemH * 0.48f)
                lineTo(cx + armW / 2f, cy - stemH * 0.48f)
                lineTo(cx + armW / 2f, cy + stemH * 0.52f)
                lineTo(cx - armW / 2f, cy + stemH * 0.52f)
                close()
            },
            color = crossColor,
        )
        drawPath(
            Path().apply {
                moveTo(cx - crossArmW / 2f, cy - crossArmH * 0.2f)
                lineTo(cx + crossArmW / 2f, cy - crossArmH * 0.2f)
                lineTo(cx + crossArmW / 2f, cy + crossArmH * 0.8f)
                lineTo(cx - crossArmW / 2f, cy + crossArmH * 0.8f)
                close()
            },
            color = crossColor,
        )
    }
}

private fun settingsHeartPath(width: Float, height: Float): Path = Path().apply {
    val left = width * 0.10f
    val right = width * 0.90f
    val bottom = height * 0.94f
    val midX = width * 0.5f
    val midY = height * 0.36f

    moveTo(midX, midY)
    cubicTo(
        width * 0.22f, height * 0.04f,
        left, height * 0.18f,
        left, height * 0.46f,
    )
    cubicTo(
        left, height * 0.68f,
        width * 0.32f, height * 0.84f,
        midX, bottom,
    )
    cubicTo(
        width * 0.68f, height * 0.84f,
        right, height * 0.68f,
        right, height * 0.46f,
    )
    cubicTo(
        right, height * 0.18f,
        width * 0.78f, height * 0.04f,
        midX, midY,
    )
    close()
}

private fun DrawScope.drawWing(
    left: Boolean,
    width: Float,
    height: Float,
    fill: Color,
    bone: Color,
) {
    val sign = if (left) -1f else 1f
    val rootX = width * 0.5f + sign * width * 0.28f
    val rootY = height * 0.28f
    val tipX = width * 0.5f + sign * width * 0.50f
    val tipY = height * 0.10f
    val midX = width * 0.5f + sign * width * 0.46f
    val midY = height * 0.34f
    val lowerX = width * 0.5f + sign * width * 0.34f
    val lowerY = height * 0.42f

    val membrane = Path().apply {
        moveTo(rootX, rootY)
        lineTo(tipX, tipY)
        quadraticTo(midX, height * 0.20f, midX, midY)
        quadraticTo(width * 0.5f + sign * width * 0.36f, height * 0.38f, lowerX, lowerY)
        quadraticTo(width * 0.5f + sign * width * 0.24f, height * 0.36f, rootX, rootY + height * 0.08f)
        close()
    }
    drawPath(membrane, fill)
    drawPath(
        membrane,
        bone,
        style = Stroke(width = 1.1.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
    )

    // wing bones
    drawLine(bone, Offset(rootX, rootY), Offset(tipX, tipY), strokeWidth = 1.4.dp.toPx(), cap = StrokeCap.Round)
    drawLine(bone, Offset(rootX, rootY), Offset(midX, midY), strokeWidth = 1.1.dp.toPx(), cap = StrokeCap.Round)
    drawLine(bone, Offset(rootX, rootY + height * 0.04f), Offset(lowerX, lowerY), strokeWidth = 1.0.dp.toPx(), cap = StrokeCap.Round)
}
