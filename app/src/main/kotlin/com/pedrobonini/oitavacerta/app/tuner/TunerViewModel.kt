package com.pedrobonini.oitavacerta.app.tuner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrobonini.oitavacerta.audioengine.pitch.PitchResult
import com.pedrobonini.oitavacerta.audioengine.tuner.TunerEngine
import com.pedrobonini.oitavacerta.tuningdata.calc.PitchMath
import com.pedrobonini.oitavacerta.tuningdata.model.MidiNote
import kotlin.math.abs
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * TODO(Fase 4): comparar contra as cordas-alvo da afinação selecionada
 * (via :settings-data), não apenas a nota cromática mais próxima.
 */
class TunerViewModel @JvmOverloads constructor(
    private val tunerEngine: TunerEngine = TunerEngine(),
) : ViewModel() {

    val uiState: StateFlow<TunerUiState> = tunerEngine.pitchResult
        .map { pitch -> pitch?.let(::toUiState) ?: TunerUiState.idle() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TunerUiState.idle())

    fun onMicPermissionGranted() {
        tunerEngine.start(viewModelScope)
    }

    fun onScreenStopped() {
        tunerEngine.stop()
    }

    override fun onCleared() {
        tunerEngine.stop()
    }

    private fun toUiState(pitch: PitchResult): TunerUiState {
        val (midiNote, cents) = PitchMath.hzToNearestNoteAndCents(pitch.hz)
        val noteLabel = MidiNote(midiNote).pitchClass.label
        val status = if (abs(cents) <= IN_TUNE_TOLERANCE_CENTS) TunerStatus.IN_TUNE else TunerStatus.DETECTING
        return TunerUiState(
            status = status,
            noteLabel = noteLabel,
            hz = pitch.hz,
            cents = cents.toFloat(),
            instrumentLabel = "Guitarra",
        )
    }

    private companion object {
        const val IN_TUNE_TOLERANCE_CENTS = 5.0
    }
}
