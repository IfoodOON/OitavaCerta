package com.pedrobonini.oitavacerta.app.uicommon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

/** Moldura com cantos em L, estilo HUD, usada nos placeholders de anúncio e módulos técnicos. */
@Composable
fun HudCornerBox(modifier: Modifier = Modifier, color: Color = LocalMatrixColors.current.neutral, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val bracket = 8.dp.toPx()
            val stroke = Stroke(width = 1.dp.toPx())
            // top-left
            drawLine(color, Offset(0f, 0f), Offset(bracket, 0f), stroke.width)
            drawLine(color, Offset(0f, 0f), Offset(0f, bracket), stroke.width)
            // top-right
            drawLine(color, Offset(size.width - bracket, 0f), Offset(size.width, 0f), stroke.width)
            drawLine(color, Offset(size.width, 0f), Offset(size.width, bracket), stroke.width)
            // bottom-left
            drawLine(color, Offset(0f, size.height - bracket), Offset(0f, size.height), stroke.width)
            drawLine(color, Offset(0f, size.height), Offset(bracket, size.height), stroke.width)
            // bottom-right
            drawLine(color, Offset(size.width - bracket, size.height), Offset(size.width, size.height), stroke.width)
            drawLine(color, Offset(size.width, size.height - bracket), Offset(size.width, size.height), stroke.width)
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
fun AdPlaceholder(label: String, modifier: Modifier = Modifier) {
    val neutral = LocalMatrixColors.current.neutral
    HudCornerBox(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        color = neutral.copy(alpha = 0.5f),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = neutral.copy(alpha = 0.6f),
        )
    }
}
