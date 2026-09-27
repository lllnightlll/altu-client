package com.example.altu.ChatBar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.altu.R
import java.io.File
import kotlin.random.Random

private fun insetX(width: Float, fraction: Float): Float =
    width * (0.05f + fraction * 0.90f)

private fun heartPath(width: Float, height: Float): Path = Path().apply {
    moveTo(insetX(width, 0.5f), height * 0.10f)
    cubicTo(
        insetX(width, 0.22f), -height * 0.10f,
        insetX(width, 0f), height * 0.06f,
        insetX(width, 0f), height * 0.44f,
    )
    cubicTo(
        insetX(width, 0f), height * 0.68f,
        insetX(width, 0.40f), height * 0.82f,
        insetX(width, 0.5f), height,
    )
    cubicTo(
        insetX(width, 0.60f), height * 0.82f,
        insetX(width, 1f), height * 0.68f,
        insetX(width, 1f), height * 0.44f,
    )
    cubicTo(
        insetX(width, 1f), height * 0.06f,
        insetX(width, 0.78f), -height * 0.10f,
        insetX(width, 0.5f), height * 0.10f,
    )
    close()
}

private val heartEdgePoints = listOf(
    0.50f to 0.08f,
    0.22f to 0.00f,
    0.10f to 0.00f,
    0.00f to 0.20f,
    0.00f to 0.44f,
    0.40f to 0.82f,
    0.50f to 0.98f,
    0.60f to 0.82f,
    1.00f to 0.44f,
    1.00f to 0.20f,
    0.90f to 0.00f,
    0.78f to 0.00f,
)

@Composable
fun HeartAvatar(
    contentDescription: String,
    @DrawableRes avatarRes: Int = R.drawable.ic_launcher_foreground,
    avatarPath: String? = null,
    avatarUri: Uri? = null,
    modifier: Modifier = Modifier,
    height: Dp = 54.dp,
    widthRatio: Float = 1.45f,
    borderWidth: Dp = 3.5.dp,
) {
    val heartShape = remember {
        GenericShape { s, _ ->
            addPath(heartPath(s.width, s.height))
        }
    }
    val diamondIndices = remember {
        List(Random.nextInt(5, 9)) { Random.nextInt(heartEdgePoints.size) }
    }
    val context = LocalContext.current
    val fileBitmap = remember(avatarPath, avatarUri) {
        decodeAvatar(avatarPath, avatarUri, context)
    }

    Box(
        modifier = modifier
            .height(height)
            .aspectRatio(widthRatio)
            .heartFrame(diamondIndices, borderWidth)
    ) {
        val imageModifier = Modifier
            .fillMaxSize()
            .padding(1.5.dp)
            .clip(heartShape)
        if (fileBitmap != null) {
            Image(
                bitmap = fileBitmap,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = imageModifier,
            )
        } else {
            Image(
                painter = painterResource(avatarRes),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = imageModifier,
            )
        }
    }
}

private fun Modifier.heartFrame(
    diamondIndices: List<Int>,
    borderWidth: Dp,
    lineColor: Color = Color(0xFFACADAC),
): Modifier = drawWithContent {
    drawContent()

    val stroke = borderWidth.toPx()
    val outerRadius = 4.5.dp.toPx()
    val innerRadius = 2.2.dp.toPx()
    val heart = heartPath(size.width, size.height)

    drawPath(
        path = heart,
        color = lineColor,
        style = Stroke(width = stroke),
    )

    diamondIndices.forEach { index ->
        val (nx, ny) = heartEdgePoints[index % heartEdgePoints.size]
        val center = Offset(insetX(size.width, nx), ny * size.height)
        drawRhombus(center, outerRadius, lineColor)
        drawRhombus(center, innerRadius, Color.Black)
    }
}

private fun decodeAvatar(
    avatarPath: String?,
    avatarUri: Uri?,
    context: android.content.Context,
): ImageBitmap? {
    if (!avatarPath.isNullOrBlank() && File(avatarPath).exists()) {
        return BitmapFactory.decodeFile(avatarPath)?.asImageBitmap()
    }
    if (avatarUri == null) return null
    return try {
        context.contentResolver.openInputStream(avatarUri)?.use { input ->
            BitmapFactory.decodeStream(input)?.asImageBitmap()
        }
    } catch (_: Exception) {
        null
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
