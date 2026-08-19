package com.pedrobonini.oitavacerta.app.metronome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "$bpm",
            style = MaterialTheme.typography.displayLarge,
            color = if (isPlaying) matrixColors.inTune else matrixColors.neutral,
        )
        Text(
            text = "BPM",
            style = MaterialTheme.typography.labelSmall,
            color = matrixColors.neutral,
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedButton(onClick = { viewModel.setBpm(bpm - 5) }) { Text("-5") }
            OutlinedButton(onClick = { viewModel.setBpm(bpm - 1) }) { Text("-1") }
            OutlinedButton(onClick = { viewModel.setBpm(bpm + 1) }) { Text("+1") }
            OutlinedButton(onClick = { viewModel.setBpm(bpm + 5) }) { Text("+5") }
        }

        Row(
            modifier = Modifier.padding(top = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(beatsPerMeasure) { index ->
                val isActive = isPlaying && index == currentBeat
                val dotColor = if (isActive) matrixColors.inTune else matrixColors.neutral.copy(alpha = 0.3f)
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(dotColor),
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Compasso:", style = MaterialTheme.typography.bodyMedium, color = matrixColors.neutral)
            OutlinedButton(onClick = { viewModel.setBeatsPerMeasure(beatsPerMeasure - 1) }) { Text("-") }
            Text("$beatsPerMeasure", style = MaterialTheme.typography.bodyLarge, color = matrixColors.neutral)
            OutlinedButton(onClick = { viewModel.setBeatsPerMeasure(beatsPerMeasure + 1) }) { Text("+") }
        }

        OutlinedButton(
            onClick = { viewModel.togglePlay() },
            modifier = Modifier.padding(top = 32.dp),
        ) {
            Text(
                text = if (isPlaying) "PARAR" else "TOCAR",
                color = if (isPlaying) matrixColors.detecting else matrixColors.inTune,
            )
        }
    }
}
