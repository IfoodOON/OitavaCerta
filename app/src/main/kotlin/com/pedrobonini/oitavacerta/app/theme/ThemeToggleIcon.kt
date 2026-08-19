package com.pedrobonini.oitavacerta.app.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Lâmpada acesa = tema claro; apagada = tema escuro. Clicar alterna.
 */
@Composable
fun ThemeToggleIcon(
    isLightTheme: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .size(28.dp)
            .clickable(onClick = onToggle),
    ) {
        val bulbColor = if (isLightTheme) Color(0xFFFFD54F) else Color(0xFF4A4A4A)
        val bulbRadius = size.minDimension * 0.32f
        val bulbCenter = Offset(size.width / 2f, size.height * 0.4f)

        if (isLightTheme) {
            drawCircle(color = bulbColor, radius = bulbRadius, center = bulbCenter)
            drawCircle(
                color = bulbColor.copy(alpha = 0.35f),
                radius = bulbRadius * 1.8f,
                center = bulbCenter,
            )
        } else {
            drawCircle(color = bulbColor, radius = bulbRadius, center = bulbCenter, style = Stroke(width = 2.dp.toPx()))
        }

        val baseTop = bulbCenter.y + bulbRadius * 0.7f
        drawRect(
            color = bulbColor,
            topLeft = Offset(bulbCenter.x - bulbRadius * 0.4f, baseTop),
            size = androidx.compose.ui.geometry.Size(bulbRadius * 0.8f, bulbRadius * 0.6f),
            style = Stroke(width = 2.dp.toPx()),
        )
    }
}
