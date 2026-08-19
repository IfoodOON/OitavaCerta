package com.pedrobonini.oitavacerta.app.tuner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedrobonini.oitavacerta.uitheme.SpaceMono
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val MAX_CENTS = 50f
private const val SWEEP_DEGREES = 90f

/**
 * Medidor circular/radial: anel com ponteiro saindo do centro. 0 cents
 * aponta pra cima (12h); desvia +-90 graus nos extremos +-50 cents.
 */
@Composable
fun RadialGauge(
    noteLabel: String,
    cents: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val tickStyle = TextStyle(fontFamily = SpaceMono, fontSize = 12.sp, color = color)

    Box(modifier = modifier.aspectRatio(1f), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = min(size.width, size.height) / 2f * 0.85f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Glow manual: varios traços concentricos com alpha decrescente.
            val glowSteps = listOf(0.35f to 0.10f, 0.20f to 0.18f, 0.0f to 0.35f)
            glowSteps.forEach { (extraWidth, alpha) ->
                drawCircle(
                    color = color.copy(alpha = alpha),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 6.dp.toPx() + extraWidth * radius),
                )
            }

            // Marcas -50 / 0 / +50
            listOf(-MAX_CENTS, 0f, MAX_CENTS).forEach { tickCents ->
                val angleDeg = (tickCents / MAX_CENTS) * SWEEP_DEGREES - 90f
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val outer = Offset(
                    center.x + (cos(angleRad) * (radius + 6.dp.toPx())).toFloat(),
                    center.y + (sin(angleRad) * (radius + 6.dp.toPx())).toFloat(),
                )
                val label = if (tickCents == 0f) "0" else if (tickCents < 0) "-50" else "+50"
                val layout = textMeasurer.measure(label, tickStyle)
                val textX = outer.x - layout.size.width / 2f
                val textY = outer.y - layout.size.height / 2f
                drawText(layout, topLeft = Offset(textX, textY))
            }

            // Ponteiro: 0 cents = 12h (para cima), +-50 cents = +-90 graus.
            val needleAngleDeg = (cents.coerceIn(-MAX_CENTS, MAX_CENTS) / MAX_CENTS) * SWEEP_DEGREES - 90f
            val needleAngleRad = Math.toRadians(needleAngleDeg.toDouble())
            val needleLength = radius * 0.7f
            val needleEnd = Offset(
                center.x + (cos(needleAngleRad) * needleLength).toFloat(),
                center.y + (sin(needleAngleRad) * needleLength).toFloat(),
            )
            drawLine(
                color = color,
                start = center,
                end = needleEnd,
                strokeWidth = 4.dp.toPx(),
            )
        }

        Text(text = noteLabel, style = tickStyle.copy(fontSize = 64.sp, color = color))
    }
}
