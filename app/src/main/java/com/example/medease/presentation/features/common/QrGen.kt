package com.example.medease.presentation.features.common

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Displays a QR code for [content] at the desired [size].
 *
 * The QR code will render in the background before displaying. If this takes any amount of time, a circular progress
 * indicator will display until the QR code is rendered.
 */
@Composable
fun QrCodeImage(
    modifier: Modifier = Modifier,
    content: String,
    size: Dp,
) {
    Box(
        modifier = modifier
            .size(size),
        contentAlignment = Alignment.Center,
    ) {
        // QR Code Image
        val bitmap = rememberQrBitmap(content = content, size = size)

        if (bitmap != null) {
            Image(
                painter = remember(bitmap) { BitmapPainter(bitmap.asImageBitmap()) },
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(size),
            )
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun rememberQrBitmap(content: String, size: Dp): Bitmap? {
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }

    val bitmap = remember(content, sizePx) {
        mutableStateOf<Bitmap?>(null)
    }

    LaunchedEffect(content, sizePx) {
        if (bitmap.value == null) {
            withContext(Dispatchers.IO) {
                val qrCodeWriter = QRCodeWriter()
                val hints = mapOf(EncodeHintType.MARGIN to 0)
                val bitMatrix = try {
                    qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
                } catch (e: Exception) {
                    null
                }

                bitMatrix?.let { matrix ->
                    val pixels = IntArray(matrix.width * matrix.height) { index ->
                        val x = index % matrix.width
                        val y = index / matrix.width
                        if (matrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                    }

                    val bmp = Bitmap.createBitmap(matrix.width, matrix.height, Bitmap.Config.ARGB_8888)
                    bmp.setPixels(pixels, 0, matrix.width, 0, 0, matrix.width, matrix.height)
                    bitmap.value = bmp
                }
            }
        }
    }

    return bitmap.value
}



@PreviewLightDark
@Composable
fun QrCodeImagePreview() {
    QrCodeImage(
        content = "https://example.com",
        size = 256.dp,
    )
}