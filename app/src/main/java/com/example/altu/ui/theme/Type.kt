package com.example.altu.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.altu.R

val GothicFont = FontFamily(
    Font(R.font.cardgothic, FontWeight.Normal)
)

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 54.sp, lineHeight = 66.sp),
    displayMedium = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 48.sp, lineHeight = 60.sp),
    displaySmall = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 42.sp, lineHeight = 54.sp),
    headlineLarge = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 42.sp, lineHeight = 54.sp),
    headlineMedium = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 39.sp, lineHeight = 51.sp),
    headlineSmall = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 48.sp),
    titleLarge = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 33.sp, lineHeight = 42.sp),
    titleMedium = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 27.sp, lineHeight = 36.sp),
    titleSmall = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 24.sp, lineHeight = 33.sp),
    bodyLarge = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 27.sp, lineHeight = 39.sp, letterSpacing = 0.45.sp),
    bodyMedium = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 24.sp, lineHeight = 36.sp, letterSpacing = 0.3.sp),
    bodySmall = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 21.sp, lineHeight = 30.sp),
    labelLarge = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 24.sp, lineHeight = 33.sp),
    labelMedium = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 21.sp, lineHeight = 27.sp),
    labelSmall = TextStyle(fontFamily = GothicFont, fontWeight = FontWeight.Normal, fontSize = 19.5.sp, lineHeight = 24.sp),
)
