package com.example.altu.SteppedBorder

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.random.Random

private data class BorderDiamond(
    val side: Int,
    val t: Float,
)

fun Modifier.steppedBorder(
    width: Dp = 1.dp,
    color: Color = Color(0xFFACADAC),
    shape: Shape = RoundedCornerShape(16.dp),
    innerDiamondColor: Color = Color.Black,
): Modifier = composed {
    val topSteps = remember { List(5) { Random.nextInt(2, 9).toFloat() } }
    val diamonds = remember {
        List(Random.nextInt(6, 12)) {
            BorderDiamond(
                side = Random.nextInt(4),
                t = Random.nextFloat().coerceIn(0.08f, 0.92f),
            )
        }
    }

    drawWithContent {
        drawContent()

        val stroke = width.toPx()
        val inset = stroke / 2f
        val cornerRadius = shape.cornerRadius(size, layoutDirection, this)
        val borderPath = buildSteppedBorderPath(
            width = size.width,
            height = size.height,
            inset = inset,
            cornerRadius = cornerRadius,
            topSteps = topSteps,
        )

        drawPath(
            path = borderPath,
            color = color,
            style = Stroke(width = stroke),
        )

        val outerRadius = 3.dp.toPx()
        val innerRadius = 1.4.dp.toPx()
        diamonds.forEach { diamond ->
            val center = pointOnBorder(
                width = size.width,
                height = size.height,
                inset = inset,
                cornerRadius = cornerRadius,
                topSteps = topSteps,
                side = diamond.side,
                t = diamond.t,
            )
            drawRhombus(center, outerRadius, color)
            drawRhombus(center, innerRadius, innerDiamondColor)
        }
    }
}

private fun Shape.cornerRadius(
    size: androidx.compose.ui.geometry.Size,
    layoutDirection: LayoutDirection,
    scope: DrawScope,
): Float {
    val outline = createOutline(size, layoutDirection, scope)
    return when (outline) {
        is Outline.Rounded -> outline.roundRect.topLeftCornerRadius.x
        else -> 0f
    }
}

private fun buildSteppedBorderPath(
    width: Float,
    height: Float,
    inset: Float,
    cornerRadius: Float,
    topSteps: List<Float>,
): Path {
    val r = min(cornerRadius, min(width, height) / 2f - inset)
    val left = inset
    val top = inset
    val right = width - inset
    val bottom = height - inset
    val topStart = left + r
    val topEnd = right - r
    val segCount = topSteps.size
    val segW = (topEnd - topStart) / segCount

    return Path().apply {
        moveTo(left + r, bottom)
        lineTo(right - r, bottom)
        quadraticTo(right, bottom, right, bottom - r)
        lineTo(right, top + r)
        quadraticTo(
            right,
            top + topSteps.last(),
            topEnd,
            top + topSteps.last(),
        )

        for (i in segCount - 1 downTo 1) {
            lineTo(topStart + i * segW, top + topSteps[i])
            lineTo(topStart + i * segW, top + topSteps[i - 1])
        }
        lineTo(topStart, top + topSteps.first())

        quadraticTo(
            left,
            top + topSteps.first(),
            left,
            top + r,
        )
        lineTo(left, bottom - r)
        quadraticTo(left, bottom, left + r, bottom)
        close()
    }
}

private fun pointOnBorder(
    width: Float,
    height: Float,
    inset: Float,
    cornerRadius: Float,
    topSteps: List<Float>,
    side: Int,
    t: Float,
): Offset {
    val r = min(cornerRadius, min(width, height) / 2f - inset)
    val left = inset
    val top = inset
    val right = width - inset
    val bottom = height - inset
    val topStart = left + r
    val topEnd = right - r
    val segCount = topSteps.size
    val segW = (topEnd - topStart) / segCount

    return when (side) {
        0 -> {
            val scaled = t * segCount
            val index = scaled.toInt().coerceIn(0, segCount - 1)
            val local = scaled - index
            Offset(
                x = topStart + segW * (index + local),
                y = top + topSteps[index],
            )
        }
        1 -> Offset(
            x = right,
            y = top + r + (bottom - top - 2 * r) * t,
        )
        2 -> Offset(
            x = right - r - (right - left - 2 * r) * t,
            y = bottom,
        )
        else -> Offset(
            x = left,
            y = bottom - r - (bottom - top - 2 * r) * t,
        )
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
