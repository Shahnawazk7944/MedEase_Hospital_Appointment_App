package com.example.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import kotlin.collections.get

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String? = null,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop,
    loaderSize: Dp = 20.dp,
    loaderPadding: Dp = 4.dp
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = url,
            modifier = Modifier.matchParentSize(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            loading = {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(loaderSize)
                        .padding(loaderPadding),
                    color = MaterialTheme.colorScheme.primary
                )
            },
        )
        overlay?.let { it() }
    }
}
