package com.example.altu.SoundBar

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class JitterShadow(
    val offsetX: Dp,
    val offsetY: Dp,
    val blur: Dp,
    val alpha: Float
)

fun jitterShadow(): JitterShadow = JitterShadow(
    offsetX = Random.nextInt(-5, 6).dp,
    offsetY = Random.nextInt(2, 8).dp,
    blur = Random.nextInt(4, 12).dp,
    alpha = 0.28f + Random.nextFloat() * 0.4f
)

fun Modifier.randomShadow(shadow: JitterShadow? = null): Modifier = composed {
    val resolved = shadow ?: remember { jitterShadow() }
    drawBehind {
        val paint = Paint().apply {
            color = Color.Black.copy(alpha = resolved.alpha)
            style = PaintingStyle.Fill
            asFrameworkPaint().apply {
                isAntiAlias = true
                color = Color.Black.copy(alpha = resolved.alpha).toArgb()
                maskFilter = BlurMaskFilter(resolved.blur.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }
        drawIntoCanvas { canvas ->
            canvas.drawRoundRect(
                left = resolved.offsetX.toPx(),
                top = resolved.offsetY.toPx(),
                right = size.width + resolved.offsetX.toPx(),
                bottom = size.height + resolved.offsetY.toPx(),
                radiusX = 4.dp.toPx(),
                radiusY = 4.dp.toPx(),
                paint = paint
            )
        }
    }
}
