package com.pedrobonini.oitavacerta.app.tuner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors
import java.util.Locale

@Composable
fun TunerScreen(
    modifier: Modifier = Modifier,
    viewModel: TunerViewModel = viewModel(),
) {
    val context = LocalContext.current
    var hasMicPermission by remember {
        mutableStateOf(
            context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted -> hasMicPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasMicPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    DisposableEffect(hasMicPermission) {
        if (hasMicPermission) {
            viewModel.onMicPermissionGranted()
        }
        onDispose { viewModel.onScreenStopped() }
    }

    val matrixColors = LocalMatrixColors.current
    val state by viewModel.uiState.collectAsState()
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
        if (!hasMicPermission) {
            Text(
                text = "Toque para permitir o microfone",
                style = MaterialTheme.typography.bodyLarge,
                color = matrixColors.neutral,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }

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
