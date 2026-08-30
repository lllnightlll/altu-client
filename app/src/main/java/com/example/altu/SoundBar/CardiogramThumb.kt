package com.example.altu.SoundBar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp

@Composable
fun CardiogramThumb(
    color: Color,
    modifier: Modifier = Modifier,
) {
    val shadow = remember { jitterShadow() }
    Canvas(modifier.size(36.dp, 24.dp)) {
        val stroke = Stroke(
            width = 2.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val w = size.width
        val h = size.height
        val mid = h * 0.55f
        val wave = Path().apply {
            moveTo(0f, mid)
            lineTo(w * 0.12f, mid)
            lineTo(w * 0.20f, mid - h * 0.14f)
            lineTo(w * 0.28f, mid)
            lineTo(w * 0.38f, mid)
            lineTo(w * 0.42f, mid + h * 0.18f)
            lineTo(w * 0.52f, h * 0.06f)
            lineTo(w * 0.62f, h * 0.94f)
            lineTo(w * 0.70f, mid)
            lineTo(w * 0.80f, mid - h * 0.22f)
            lineTo(w * 0.90f, mid)
            lineTo(w, mid)
        }
        translate(shadow.offsetX.toPx(), shadow.offsetY.toPx()) {
            drawPath(wave, Color.Black.copy(alpha = shadow.alpha), style = stroke)
        }
        drawPath(wave, color, style = stroke)
    }
}