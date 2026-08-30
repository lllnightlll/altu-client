package com.example.altu.SoundBar

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.iconShape(edgeInset: Dp = 7.dp): Modifier = composed {
    val lineShadows = remember { List(4) { jitterShadow() } }
    val diamondShadows = remember { List(20) { jitterShadow() } }

    drawWithContent {
        drawContent()

        val lineColor = Color(0xFFACADAC)
        val innerColor = Color.Black
        val stroke = 3.dp.toPx()
        val outerRadius = 6.dp.toPx()
        val innerRadius = 2.8.dp.toPx()
        val inset = edgeInset.toPx()

        val left = inset
        val top = inset
        val right = size.width - inset
        val bottom = size.height - inset

        val sides = listOf(
            Offset(left, top) to Offset(right, top),
            Offset(right, top) to Offset(right, bottom),
            Offset(right, bottom) to Offset(left, bottom),
            Offset(left, bottom) to Offset(left, top),
        )

        sides.forEachIndexed { sideIndex, (start, end) ->
            val lineShadow = lineShadows[sideIndex]
            val lineOffset = Offset(lineShadow.offsetX.toPx(), lineShadow.offsetY.toPx())
            drawLine(
                color = Color.Black.copy(alpha = lineShadow.alpha),
                start = start + lineOffset,
                end = end + lineOffset,
                strokeWidth = stroke
            )
            drawLine(color = lineColor, start = start, end = end, strokeWidth = stroke)

            repeat(5) { index ->
                val t = index / 4f
                val center = Offset(
                    start.x + (end.x - start.x) * t,
                    start.y + (end.y - start.y) * t,
                )
                val diamondShadow = diamondShadows[sideIndex * 5 + index]
                val diamondOffset = Offset(diamondShadow.offsetX.toPx(), diamondShadow.offsetY.toPx())
                drawRhombus(
                    center + diamondOffset,
                    outerRadius,
                    Color.Black.copy(alpha = diamondShadow.alpha)
                )
                drawRhombus(center, outerRadius, lineColor)
                drawRhombus(center, innerRadius, innerColor)
            }
        }
    }
}

private fun DrawScope.drawRhombus(center: Offset, radius: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        lineTo(center.x + radius, center.y)
        lineTo(center.x, center.y + radius)
        lineTo(center.x - radius, center.y)
        close()
    }
    drawPath(path, color)
}
