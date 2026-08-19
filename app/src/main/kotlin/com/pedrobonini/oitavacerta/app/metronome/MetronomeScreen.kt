package com.pedrobonini.oitavacerta.app.metronome

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pedrobonini.oitavacerta.app.uicommon.MatrixButton
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors

@Composable
fun MetronomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MetronomeViewModel = viewModel(),
) {
    val matrixColors = LocalMatrixColors.current
    val bpm by viewModel.bpm.collectAsState()
    val beatsPerMeasure by viewModel.beatsPerMeasure.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentBeat by viewModel.currentBeat.collectAsState()

    DisposableEffect(Unit) {
        onDispose { if (isPlaying) viewModel.togglePlay() }
    }

    val accentColor = if (isPlaying) matrixColors.inTune else matrixColors.neutral

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center,
        ) {
            val flashOn = isPlaying && currentBeat >= 0
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2f
                drawCircle(
                    color = matrixColors.neutral.copy(alpha = 0.25f),
                    radius = radius,
                    style = Stroke(width = 1.dp.toPx()),
                )
                drawCircle(
                    color = matrixColors.neutral.copy(alpha = 0.3f),
                    radius = radius * 0.75f,
                    style = Stroke(width = 1.dp.toPx()),
                )
                drawCircle(
                    color = accentColor.copy(alpha = if (flashOn) 0.9f else 0.5f),
                    radius = radius * 0.55f,
                    style = Stroke(width = 2.dp.toPx()),
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$bpm",
                    style = TextStyle(
                        fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
                        fontSize = 48.sp,
                        color = accentColor,
                        shadow = Shadow(color = accentColor, blurRadius = 30f),
                    ),
                )
                Text(text = "BPM", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 3.sp), color = matrixColors.neutral)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SquareIconButton(icon = Icons.Filled.Remove, color = matrixColors.neutral, onClick = { viewModel.setBpm(bpm - 1) })
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$beatsPerMeasure/4",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                    color = matrixColors.neutral,
                )
                Text(text = "COMPASSO", style = MaterialTheme.typography.labelSmall, color = matrixColors.neutral.copy(alpha = 0.6f))
            }
            SquareIconButton(icon = Icons.Filled.Add, color = matrixColors.neutral, onClick = { viewModel.setBpm(bpm + 1) })
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(beatsPerMeasure) { index ->
                val dotColor = if (isPlaying && index == currentBeat) matrixColors.inTune else matrixColors.neutral.copy(alpha = 0.3f)
                Box(modifier = Modifier.size(6.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(color = dotColor)
                    }
                }
            }
        }

        MatrixButton(
            text = if (isPlaying) "PARAR PULSO" else "INICIAR PULSO",
            color = matrixColors.inTune,
            onClick = { viewModel.togglePlay() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
        )
    }
}

@Composable
private fun SquareIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .then(Modifier.padding(0.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = color.copy(alpha = 0.5f), style = Stroke(width = 1.dp.toPx()))
        }
        androidx.compose.material3.IconButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
        }
    }
}
