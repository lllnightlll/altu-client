package com.example.altu.Routes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun SettingsTabIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
) {
    val strokeColor = if (selected) Color(0xFFD4B8D8) else Color(0xFFb488a1)

    Canvas(modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val cx = w * 0.5f
        val cy = h * 0.5f
        val stroke = Stroke(
            width = 1.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        )

        drawPath(gearOutlinePath(cx, cy, min(w, h)), strokeColor, style = stroke)
        drawPath(heartOutlinePath(w, h), strokeColor, style = stroke)
    }
}

private fun gearOutlinePath(cx: Float, cy: Float, size: Float): Path {
    val outerR = size * 0.46f
    val innerR = size * 0.34f
    val teeth = 8
    val toothHalf = Math.PI / teeth / 2.4
    val path = Path()

    for (i in 0 until teeth) {
        val a = -Math.PI / 2.0 + i * (2.0 * Math.PI / teeth)
        val a0 = a - toothHalf
        val a1 = a - toothHalf * 0.35
        val a2 = a + toothHalf * 0.35
        val a3 = a + toothHalf

        val p0 = polar(cx, cy, innerR, a0)
        val p1 = polar(cx, cy, outerR, a1)
        val p2 = polar(cx, cy, outerR, a2)
        val p3 = polar(cx, cy, innerR, a3)

        if (i == 0) path.moveTo(p0.x, p0.y) else path.lineTo(p0.x, p0.y)
        path.lineTo(p1.x, p1.y)
        path.lineTo(p2.x, p2.y)
        path.lineTo(p3.x, p3.y)
    }
    path.close()
    return path
}

private fun polar(cx: Float, cy: Float, r: Float, angle: Double): Offset =
    Offset(
        cx + (cos(angle) * r).toFloat(),
        cy + (sin(angle) * r).toFloat(),
    )

private fun heartOutlinePath(width: Float, height: Float): Path = Path().apply {
    val left = width * 0.32f
    val right = width * 0.68f
    val bottom = height * 0.72f
    val midX = width * 0.5f
    val cleftY = height * 0.42f

    moveTo(midX, cleftY)
    cubicTo(
        width * 0.38f, height * 0.26f,
        left, height * 0.34f,
        left, height * 0.48f,
    )
    cubicTo(
        left, height * 0.60f,
        width * 0.40f, height * 0.68f,
        midX, bottom,
    )
    cubicTo(
        width * 0.60f, height * 0.68f,
        right, height * 0.60f,
        right, height * 0.48f,
    )
    cubicTo(
        right, height * 0.34f,
        width * 0.62f, height * 0.26f,
        midX, cleftY,
    )
    close()
}
