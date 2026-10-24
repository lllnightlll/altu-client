package com.example.altu.NewContact

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QrBitmap {
    fun encode(
        payload: String,
        sizePx: Int,
        ink: Color = Color(0xFF070809),
        accent: Color = Color(0xFFACADAC),
    ): Bitmap {
        val matrix = QRCodeWriter().encode(
            payload,
            BarcodeFormat.QR_CODE,
            sizePx,
            sizePx,
            mapOf(
                EncodeHintType.CHARACTER_SET to "UTF-8",
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
                EncodeHintType.MARGIN to 1,
            ),
        )
        val pixels = IntArray(sizePx * sizePx)
        val on = accent.toArgb()
        val off = ink.toArgb()
        for (y in 0 until sizePx) {
            for (x in 0 until sizePx) {
                pixels[y * sizePx + x] = if (matrix.get(x, y)) on else off
            }
        }
        return Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888).also {
            it.setPixels(pixels, 0, sizePx, 0, 0, sizePx, sizePx)
        }
    }
}
