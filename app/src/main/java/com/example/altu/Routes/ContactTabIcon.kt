package com.example.altu.Routes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.altu.R

@Composable
fun ContactTabIcon(
    selected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
) {
    val brightness = if (selected) 1.12f else 1f
    val matrix = ColorMatrix(
        floatArrayOf(
            brightness, 0f, 0f, 0f, 0f,
            0f, brightness, 0f, 0f, 0f,
            0f, 0f, brightness, 0f, 0f,
            0f, 0f, 0f, 1f, 0f,
        )
    )

    Image(
        painter = painterResource(R.drawable.gothic_cross_contact),
        contentDescription = "New Contact",
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.colorMatrix(matrix),
    )
}
