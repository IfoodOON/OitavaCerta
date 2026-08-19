package com.pedrobonini.oitavacerta.app.tuner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pedrobonini.oitavacerta.app.instrumentpicker.QuickInstrumentSheet
import com.pedrobonini.oitavacerta.app.instrumentpicker.displayName
import com.pedrobonini.oitavacerta.app.uicommon.MatrixButton
import com.pedrobonini.oitavacerta.tuningdata.model.InstrumentKey
import com.pedrobonini.oitavacerta.tuningdata.model.TuningPreset
import com.pedrobonini.oitavacerta.uitheme.LocalMatrixColors
import java.util.Locale

@Composable
fun TunerScreen(
    instrument: InstrumentKey,
    tuning: TuningPreset?,
    onInstrumentSelected: (InstrumentKey) -> Unit,
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

    LaunchedEffect(tuning) {
        viewModel.updateTargetTuning(tuning)
    }

    var showInstrumentSheet by remember { mutableStateOf(false) }

    val matrixColors = LocalMatrixColors.current
    val state by viewModel.uiState.collectAsState()
    val color = when (state.status) {
        TunerStatus.IDLE -> matrixColors.neutral
        TunerStatus.DETECTING -> matrixColors.detecting
        TunerStatus.IN_TUNE -> matrixColors.inTune
    }
    val glow = Shadow(color = color, blurRadius = 40f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (!hasMicPermission) {
            Text(
                text = "toque para permitir o microfone",
                style = MaterialTheme.typography.bodyMedium,
                color = matrixColors.neutral,
                modifier = Modifier.padding(bottom = 16.dp),
            )
        }

        Box {
            Text(
                text = state.noteLabel,
                style = TextStyle(
                    fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
                    fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
                    fontSize = 110.sp,
                    color = color,
                    shadow = glow,
                ),
            )
            state.octave?.let { octave ->
                Text(
                    text = "$octave",
                    style = MaterialTheme.typography.labelLarge.copy(color = color, shadow = glow),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp),
                )
            }
        }

        HorizontalTunerMeter(
            cents = state.cents,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        FrequencyReadout(text = if (state.hz > 0) String.format(Locale.US, "%.2f", state.hz) else "0.00", unit = "HZ", color = color, glow = glow)

        if (state.status == TunerStatus.IN_TUNE) {
            Text(
                text = "STATUS: LOCKED",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                color = color,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        MatrixButton(
            text = instrument.type.displayName().uppercase(),
            color = matrixColors.inTune,
            onClick = { showInstrumentSheet = true },
            modifier = Modifier.padding(top = 32.dp),
        )
    }

    if (showInstrumentSheet) {
        QuickInstrumentSheet(
            selected = instrument,
            onSelect = onInstrumentSelected,
            onDismiss = { showInstrumentSheet = false },
        )
    }
}

@Composable
private fun FrequencyReadout(text: String, unit: String, color: androidx.compose.ui.graphics.Color, glow: Shadow) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.padding(top = 24.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp, color = color, shadow = glow),
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.labelSmall.copy(color = color.copy(alpha = 0.8f)),
            modifier = Modifier.padding(bottom = 4.dp),
        )
    }
}
