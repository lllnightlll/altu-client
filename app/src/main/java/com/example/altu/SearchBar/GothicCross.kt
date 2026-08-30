package com.example.altu.SearchBar

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GothicCross(
    color: Color = Color(0xFFACADAC),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Canvas(modifier.size(22.dp, 30.dp)) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.38f
        val arm = w * 0.13f
        val flare = w * 0.22f
        val tip = w * 0.18f

        val cross = Path().apply {
            moveTo(cx, 0f)
            lineTo(cx + flare, cy - arm)
            lineTo(cx + arm, cy - arm)
            lineTo(cx + arm, cy - arm * 0.35f)
            lineTo(w - tip, cy - flare)
            lineTo(w, cy)
            lineTo(w - tip, cy + flare)
            lineTo(cx + arm, cy + arm * 0.35f)
            lineTo(cx + arm, cy + arm)
            lineTo(cx + flare, h - tip)
            lineTo(cx, h)
            lineTo(cx - flare, h - tip)
            lineTo(cx - arm, cy + arm)
            lineTo(cx - arm, cy + arm * 0.35f)
            lineTo(tip, cy + flare)
            lineTo(0f, cy)
            lineTo(tip, cy - flare)
            lineTo(cx - arm, cy - arm * 0.35f)
            lineTo(cx - arm, cy - arm)
            lineTo(cx - flare, cy - arm)
            close()
        }

        drawPath(cross, color, style = Fill)
        drawPath(
            cross,
            Color.Black,
            style = Stroke(
                width = 1.2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        val diamond = Path().apply {
            val r = arm * 0.85f
            moveTo(cx, cy - r)
            lineTo(cx + r, cy)
            lineTo(cx, cy + r)
            lineTo(cx - r, cy)
            close()
        }
        drawPath(diamond, Color.Black)
        drawLine(
            color = Color.Black,
            start = Offset(cx, cy - arm * 1.6f),
            end = Offset(cx, cy + arm * 1.6f),
            strokeWidth = 1.1.dp.toPx()
        )
        drawLine(
            color = Color.Black,
            start = Offset(cx - arm * 1.6f, cy),
            end = Offset(cx + arm * 1.6f, cy),
            strokeWidth = 1.1.dp.toPx()
        )
    }
}
