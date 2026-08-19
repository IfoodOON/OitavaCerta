package com.pedrobonini.oitavacerta.app.tuner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private const val MAX_CENTS = 50f
private val TICKS = listOf(-50f, -40f, -30f, -20f, -10f, 0f, 10f, 20f, 30f, 40f, 50f)

/**
 * Régua horizontal com marcas de -50 a +50 cents e um ponteiro vertical
 * que desliza da esquerda pra direita conforme o desvio da nota. Réplica
 * do medidor HUD do design de referência (linha + tick marks + needle
 * vertical), não um mostrador circular.
 */
@Composable
fun HorizontalTunerMeter(
    cents: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
    ) {
        val baselineY = size.height - 8.dp.toPx()
        val usableWidth = size.width * 0.85f
        val startX = (size.width - usableWidth) / 2f

        // Linha base
        drawLine(
            color = color.copy(alpha = 0.3f),
            start = Offset(startX, baselineY),
            end = Offset(startX + usableWidth, baselineY),
            strokeWidth = 1.dp.toPx(),
        )

        // Marcas
        TICKS.forEach { tick ->
            val x = startX + (tick + MAX_CENTS) / (2 * MAX_CENTS) * usableWidth
            val isCenter = tick == 0f
            val tickHeight = if (isCenter) 20.dp.toPx() else 10.dp.toPx()
            drawLine(
                color = if (isCenter) color.copy(alpha = 0.8f) else color.copy(alpha = 0.4f),
                start = Offset(x, baselineY),
                end = Offset(x, baselineY - tickHeight),
                strokeWidth = if (isCenter) 2.dp.toPx() else 1.dp.toPx(),
            )
        }

        // Ponteiro: glow manual (linhas concentricas mais finas/mais transparentes por baixo)
        val needleX = startX + (cents.coerceIn(-MAX_CENTS, MAX_CENTS) + MAX_CENTS) / (2 * MAX_CENTS) * usableWidth
        val needleTop = baselineY - 40.dp.toPx()
        listOf(6.dp.toPx() to 0.15f, 3.dp.toPx() to 0.35f).forEach { (width, alpha) ->
            drawLine(
                color = color.copy(alpha = alpha),
                start = Offset(needleX, baselineY),
                end = Offset(needleX, needleTop),
                strokeWidth = width,
            )
        }
        drawLine(
            color = color,
            start = Offset(needleX, baselineY),
            end = Offset(needleX, needleTop),
            strokeWidth = 2.dp.toPx(),
        )
    }
}
