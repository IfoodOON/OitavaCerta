package com.pedrobonini.oitavacerta.app.tuner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors
import java.util.Locale

private enum class TunerStatus { IDLE, DETECTING, IN_TUNE }

private data class TunerUiState(
    val status: TunerStatus,
    val noteLabel: String,
    val hz: Double,
    val cents: Float,
    val instrumentLabel: String,
)

// TODO(Fase 2/3): substituir por TunerViewModel observando TunerEngine (:audio-engine).
private val demoIdleState = TunerUiState(
    status = TunerStatus.IDLE,
    noteLabel = "--",
    hz = 0.0,
    cents = 0f,
    instrumentLabel = "Guitarra",
)

@Composable
fun TunerScreen(modifier: Modifier = Modifier) {
    val matrixColors = LocalMatrixColors.current
    val state = demoIdleState
    val color = when (state.status) {
        TunerStatus.IDLE -> matrixColors.neutral
        TunerStatus.DETECTING -> matrixColors.detecting
        TunerStatus.IN_TUNE -> matrixColors.inTune
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        RadialGauge(
            noteLabel = state.noteLabel,
            cents = state.cents,
            color = color,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(bottom = 16.dp),
        )

        Text(
            text = if (state.hz > 0) String.format(Locale.US, "%.2f Hz", state.hz) else "0.0 Hz",
            style = MaterialTheme.typography.bodyLarge,
            color = color,
        )

        if (state.status == TunerStatus.IN_TUNE) {
            Text(
                text = "STATUS: LOCKED",
                style = MaterialTheme.typography.labelSmall,
                color = color,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        InstrumentChip(
            instrumentLabel = state.instrumentLabel,
            onClick = { /* TODO(Fase 4): abrir seletor rápido de instrumento */ },
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}
